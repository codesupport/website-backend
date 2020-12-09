package dev.codesupport.web.api.service;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.PublishedArticleEntity;
import dev.codesupport.web.api.data.entity.TagEntity;
import dev.codesupport.web.api.data.entity.TagSetEntity;
import dev.codesupport.web.api.data.entity.TagSetToTagEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.PublishedArticleRepository;
import dev.codesupport.web.api.data.repository.TagRepository;
import dev.codesupport.web.api.data.repository.TagSetRepository;
import dev.codesupport.web.api.data.repository.TagSetToTagsRepository;
import dev.codesupport.web.common.exception.DuplicateEntryException;
import dev.codesupport.web.common.exception.MalformedDataException;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import dev.codesupport.web.domain.VoidMethodResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    private final CrudOperations<ArticleEntity, Article, Long> crudOperations;
    private final PublishedArticleRepository publishedArticleRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(
            PublishedArticleRepository publishedArticleRepository,
            ArticleRepository articleRepository,
            TagSetToTagsRepository tagSetToTagsRepository,
            TagSetRepository tagSetRepository,
            TagRepository tagRepository
    ) {
        crudOperations = new CrudOperations<>(articleRepository, ArticleEntity.class, Article.class);
        CrudLogic crudLogic = new CrudLogic(tagRepository, tagSetRepository, tagSetToTagsRepository);
        crudOperations.setPreSaveEntities(crudLogic.preSaveLogic());
        crudOperations.setPreGetEntities(crudLogic.preGetLogic());
        this.publishedArticleRepository = publishedArticleRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<PublishedArticle> findAllArticles(boolean publishedOnly) {
        List<PublishedArticleEntity> entities;

        if (publishedOnly) {
            entities = publishedArticleRepository.findAllByPublishedIsTrue();
        } else {
            entities = publishedArticleRepository.findAll();
        }

        // For each published article entity, find the article it points to and return a PublishedArticle DTO
        return entities.stream().map(entity -> {
            Article article = crudOperations.getById(entity.getArticleId());
            PublishedArticle publishedArticle = MappingUtils.convertToType(entity, PublishedArticle.class);
            publishedArticle.setArticle(article);
            return publishedArticle;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Article> findAllArticleRevisionsById(Long id) {
        PublishedArticleEntity entity = publishedArticleRepository.getById(id);

        List<ArticleEntity> entities = articleRepository.findAllByArticleCode(entity.getArticleCode());

        return MappingUtils.convertToType(entities, Article.class);
    }

    @Override
    public PublishedArticle getArticleById(Long id) {
        PublishedArticleEntity entity = publishedArticleRepository.getById(id);
        PublishedArticle publishedArticle = MappingUtils.convertToType(entity, PublishedArticle.class);
        publishedArticle.setArticle(crudOperations.getById(entity.getArticleId()));
        return publishedArticle;
    }

    @Override
    public PublishedArticle createArticle(Article article) {
        if (!articleRepository.existsByTitleIgnoreCase(article.getTitle())) {
            generateArticleCode(article);

            Article newArticle = crudOperations.createEntity(article);

            PublishedArticleEntity entity = new PublishedArticleEntity();
            entity.setArticleCode(newArticle.getArticleCode());
            entity.setArticleId(newArticle.getId());
            entity.setPublished(false);

            PublishedArticleEntity savedEntity = publishedArticleRepository.save(entity);

            // Pulling from db to get complete data
            return getArticleById(savedEntity.getId());
        } else {
            throw new DuplicateEntryException("Article already exists with the given title");
        }
    }

    @Override
    public Article updateArticle(Article article) {
        return crudOperations.updateEntity(article);
    }

    @Override
    public VoidMethodResponse deleteArticle(Article article) {
        return new VoidMethodResponse("Not implemented", 0);
    }

    @Override
    public VoidMethodResponse publishArticle(PublishedArticle publishedArticle) {
        PublishedArticleEntity existingPublishedArticle = publishedArticleRepository.getById(publishedArticle.getId());
        ArticleEntity existingArticle = articleRepository.getById(existingPublishedArticle.getArticleId());

        // Stale data check against existing article
        if (publishedArticle.getArticle().getUpdatedOn().equals(existingArticle.getUpdatedOn())) {
            existingPublishedArticle.setPublished(true);

            publishedArticleRepository.save(existingPublishedArticle);
        } else {
            throw new MalformedDataException("Article data is stale, try again.");
        }

        return new VoidMethodResponse("Publish article", 1);
    }

    @VisibleForTesting
    void generateArticleCode(Article article) {
        do {
            article.setArticleCode(RandomStringUtils.randomAlphabetic(20));
        } while (publishedArticleRepository.existsByArticleCode(article.getArticleCode()));
    }

    static class CrudLogic {

        private final TagRepository tagRepository;
        private final TagSetRepository tagSetRepository;
        private final TagSetToTagsRepository tagSetToTagsRepository;

        private CrudLogic(
                TagRepository tagRepository,
                TagSetRepository tagSetRepository,
                TagSetToTagsRepository tagSetToTagsRepository
        ) {
            this.tagRepository = tagRepository;
            this.tagSetRepository = tagSetRepository;
            this.tagSetToTagsRepository = tagSetToTagsRepository;
        }

        @VisibleForTesting
        Consumer<ArticleEntity> preSaveLogic() {
            return this::updateTagReferences;
        }

        @VisibleForTesting
        Consumer<ArticleEntity> preGetLogic() {
            return (ArticleEntity articleEntity) -> {
                TagSetEntity tagSet = tagSetRepository.getById(articleEntity.getTagSetId());
                List<TagSetToTagEntity> xMaps = tagSetToTagsRepository.findAllByTagSetIdOrderByTagId(tagSet.getId());
                List<TagEntity> tags = xMaps.stream().map(TagSetToTagEntity::getTag).collect(Collectors.toList());
                tagSet.setTags(tags);
                articleEntity.setTagSet(tagSet);
            };
        }

        @VisibleForTesting
        void updateTagReferences(ArticleEntity articleEntity) {
            TagSetEntity tagSetEntity = articleEntity.getTagSet();
            List<TagEntity> tagEntities = tagSetEntity.getTags();

            List<String> tagLabels = tagEntities.stream().map(TagEntity::getLabel).collect(Collectors.toList());

            List<TagEntity> existingTags = tagRepository.findAllByLabelInOrderByLabel(tagLabels);

            List<TagEntity> adjustedTagEntities = new ArrayList<>();

            for (String label : tagLabels) {
                Optional<TagEntity> optional = existingTags.stream()
                        .filter(tag -> StringUtils.equals(label, tag.getLabel()))
                        .findFirst();

                if (optional.isPresent()) {
                    adjustedTagEntities.add(optional.get());
                } else {
                    TagEntity newTag = new TagEntity();
                    newTag.setLabel(label);
                    adjustedTagEntities.add(newTag);
                }
            }

            List<TagEntity> createdTagEntities = tagRepository.saveAll(adjustedTagEntities);
            tagSetEntity.setTags(createdTagEntities);
            TagSetEntity createdTagSetEntity = tagSetRepository.save(new TagSetEntity());
            articleEntity.setTagSetId(createdTagSetEntity.getId());

            for (TagEntity tag : createdTagEntities) {
                TagSetToTagEntity xMap = new TagSetToTagEntity();
                xMap.setTagSet(createdTagSetEntity);
                xMap.setTag(tag);
                tagSetToTagsRepository.save(xMap);
            }
        }

    }
}

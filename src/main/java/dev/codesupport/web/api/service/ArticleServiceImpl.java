package dev.codesupport.web.api.service;

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
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.Article;
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
        CrudLogic crudLogic = new CrudLogic(articleRepository, tagRepository, tagSetRepository, tagSetToTagsRepository);
        crudOperations.setPreSaveEntities(crudLogic.preSaveLogic());
        crudOperations.setPreGetEntities(crudLogic.preGetLogic());
        this.publishedArticleRepository = publishedArticleRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> findAllArticles() {
        return crudOperations.getAll();
    }

    @Override
    public Article getArticleById(Long id) {
        return crudOperations.getById(id);
    }

    @Override
    public Article createArticle(Article article) {
        return crudOperations.createEntity(article);
    }

    @Override
    public Article updateArticle(Article article) {
        return null;
    }

    @Override
    public Article deleteArticle(Article article) {
        return null;
    }

    @Override
    public Article publishArticle(Article article) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findById(article.getId());

        if (optionalArticle.isPresent()) {
            PublishedArticleEntity publishedArticleEntity;
            Optional<PublishedArticleEntity> optionalPA = publishedArticleRepository.findByArticleCode(optionalArticle.get().getArticleCode());

            if (optionalPA.isPresent()) {
                publishedArticleEntity = updatePublishedArticle(optionalPA.get(), optionalArticle.get());
            } else {
                publishedArticleEntity = createPublishedArticle(optionalArticle.get());
            }

            publishedArticleRepository.save(publishedArticleEntity);
        } else {
             throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }
        return null;
    }

    PublishedArticleEntity updatePublishedArticle(PublishedArticleEntity paEntity, ArticleEntity article) {
        paEntity.setArticleId(article.getId());
        return paEntity;
    }

    PublishedArticleEntity createPublishedArticle(ArticleEntity article) {
        PublishedArticleEntity newPAEntity = new PublishedArticleEntity();
        newPAEntity.setArticleId(article.getId());
        newPAEntity.setArticleCode(article.getArticleCode());
        newPAEntity.setPublished(true);
        return newPAEntity;
    }

    static class CrudLogic {

        private final ArticleRepository articleRepository;
        private final TagRepository tagRepository;
        private final TagSetRepository tagSetRepository;
        private final TagSetToTagsRepository tagSetToTagsRepository;

        private CrudLogic(
                ArticleRepository articleRepository,
                TagRepository tagRepository,
                TagSetRepository tagSetRepository,
                TagSetToTagsRepository tagSetToTagsRepository
        ) {
            this.articleRepository = articleRepository;
            this.tagRepository = tagRepository;
            this.tagSetRepository = tagSetRepository;
            this.tagSetToTagsRepository = tagSetToTagsRepository;
        }

        Consumer<ArticleEntity> preSaveLogic() {
            return (ArticleEntity articleEntity) -> {
                updateTagReferences(articleEntity);
                updateArticleMetaData(articleEntity);
            };
        }

        Consumer<ArticleEntity> preGetLogic() {
            return (ArticleEntity articleEntity) -> {
                Optional<TagSetEntity> optional = tagSetRepository.findById(articleEntity.getTagSetId());
                if (optional.isPresent()) {
                    TagSetEntity tagSet = optional.get();
                    List<TagSetToTagEntity> xMaps = tagSetToTagsRepository.findAllByTagSetIdOrderByTagId(tagSet.getId());
                    List<TagEntity> tags = xMaps.stream().map(TagSetToTagEntity::getTag).collect(Collectors.toList());
                    tagSet.setTags(tags);
                    articleEntity.setTagSet(tagSet);
                } else {
                    log.error("Article (ID: " + articleEntity.getId() + ") had no valid TagSet (ID: " + articleEntity.getTagSetId() + ")");
                }
            };
        }

        void updateArticleMetaData(ArticleEntity articleEntity) {
            Long articleId = articleEntity.getId();
            if (articleId != null && articleId != 0) {
                updateExistingArticle(articleEntity);
            } else {
                updateNewArticle(articleEntity);
            }
        }

        void updateExistingArticle(ArticleEntity articleEntity) {
            Optional<ArticleEntity> optional = articleRepository.findById(articleEntity.getId());

            if (optional.isPresent()) {
                ArticleEntity existingArticle = optional.get();
                articleEntity.setArticleCode(existingArticle.getArticleCode());

                // If the original was finalized, this is a new entry, to preserve history
                if (existingArticle.isFinalized()) {
                    articleEntity.setId(null);
                }

                articleEntity.setUpdatedOn(System.currentTimeMillis());
            } else {
                throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
            }
        }

        void updateNewArticle(ArticleEntity articleEntity) {
            // Create an ArticleId which is used to relate various versions of the same article
            articleEntity.setArticleCode(RandomStringUtils.randomAlphabetic(50));
            long time = System.currentTimeMillis();
            articleEntity.setCreatedOn(time);
            articleEntity.setUpdatedOn(time);
        }

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

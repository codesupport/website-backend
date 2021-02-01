package dev.codesupport.web.api.service;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.ImageReferenceEntity;
import dev.codesupport.web.api.data.entity.PublishedArticleEntity;
import dev.codesupport.web.api.data.entity.TagEntity;
import dev.codesupport.web.api.data.entity.TagSetEntity;
import dev.codesupport.web.api.data.entity.TagSetToTagEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.ImageReferenceRepository;
import dev.codesupport.web.api.data.repository.PublishedArticleRepository;
import dev.codesupport.web.api.data.repository.TagRepository;
import dev.codesupport.web.api.data.repository.TagSetRepository;
import dev.codesupport.web.api.data.repository.TagSetToTagsRepository;
import dev.codesupport.web.common.exception.DuplicateEntryException;
import dev.codesupport.web.common.exception.StaleDataException;
import dev.codesupport.web.common.service.service.CrudAuditableOperations;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.ImageReferenceScanner;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import dev.codesupport.web.domain.VoidMethodResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    private final CrudOperations<ArticleEntity, Article, Long> crudOperations;
    private final PublishedArticleRepository publishedArticleRepository;
    private final ImageReferenceRepository imageReferenceRepository;
    private final ArticleRepository articleRepository;
    private final ImageReferenceScanner imageReferenceScanner;

    @Autowired
    public ArticleServiceImpl(
            PublishedArticleRepository publishedArticleRepository,
            ArticleRepository articleRepository,
            ImageReferenceRepository imageReferenceRepository,
            TagSetToTagsRepository tagSetToTagsRepository,
            TagSetRepository tagSetRepository,
            TagRepository tagRepository,
            ImageReferenceScanner imageReferenceScanner
    ) {
        crudOperations = new CrudAuditableOperations<>(articleRepository, ArticleEntity.class, Article.class);
        CrudLogic crudLogic = new CrudLogic(tagRepository, tagSetRepository, tagSetToTagsRepository);
        crudOperations.setPreSaveEntities(crudLogic.preSaveLogic());
        crudOperations.setPreGetEntities(crudLogic.preGetLogic());
        this.publishedArticleRepository = publishedArticleRepository;
        this.articleRepository = articleRepository;
        this.imageReferenceRepository = imageReferenceRepository;
        this.imageReferenceScanner = imageReferenceScanner;
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
    @Transactional
    public PublishedArticle createArticle(Article article) {
        if (!articleRepository.existsByTitleIgnoreCase(article.getTitle())) {
            generateArticleCode(article);

            Article newArticle = crudOperations.createEntity(article);

            updateImageReferences(newArticle);

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
        updateArticleTransaction(article);

        // Since it's a transaction, we need to look up the data after.
        return crudOperations.getById(article.getId());
    }

    //S2230 - Doesn't need to be public
    @SuppressWarnings("java:S2230")
    @VisibleForTesting
    @Transactional
    void updateArticleTransaction(Article article) {
        updateImageReferences(article);

        crudOperations.updateEntity(article);
    }

    @Override
    public VoidMethodResponse deleteArticle(Article article) {
        return new VoidMethodResponse("Not implemented", 0);
    }

    @Override
    @Transactional
    public VoidMethodResponse publishArticle(PublishedArticle publishedArticle) {
        PublishedArticleEntity existingPublishedArticle = publishedArticleRepository.getById(publishedArticle.getId());
        int affectedEntities = 0;

        // If the publish state wasn't changed, do nothing
        if (existingPublishedArticle.isPublished() != publishedArticle.isPublished()) {
            affectedEntities++;
            ArticleEntity existingArticle = articleRepository.getById(existingPublishedArticle.getArticleId());

            // Stale data check against existing article
            if (publishedArticle.getArticle().getUpdatedOn().equals(existingArticle.getUpdatedOn())) {
                existingPublishedArticle.setPublished(publishedArticle.isPublished());

                publishedArticleRepository.save(existingPublishedArticle);
            } else {
                throw new StaleDataException();
            }
        }

        return new VoidMethodResponse("Publish article", affectedEntities);
    }

    @VisibleForTesting
    void generateArticleCode(Article article) {
        do {
            article.setArticleCode(RandomStringUtils.randomAlphabetic(20));
        } while (publishedArticleRepository.existsByArticleCode(article.getArticleCode()));
    }

    void updateImageReferences(Article article) {
        Set<ImageReferenceEntity> newReferences = scanForImageReferences(article);
        Set<ImageReferenceEntity> oldReferences = imageReferenceRepository.findAllByArticleId(article.getId());

        calculateReferenceDelta(newReferences, oldReferences);

        imageReferenceRepository.deleteAll(oldReferences);
        imageReferenceRepository.saveAll(newReferences);
    }

    Set<ImageReferenceEntity> scanForImageReferences(Article article) {
        Set<String> fileNames = imageReferenceScanner.scan(article.getContent());

        return fileNames.stream().map(fileName -> {
            ImageReferenceEntity imageReferenceEntity = new ImageReferenceEntity();
            imageReferenceEntity.setArticleId(article.getId());
            imageReferenceEntity.setImageName(fileName);
            return imageReferenceEntity;
        }).collect(Collectors.toSet());
    }

    void calculateReferenceDelta(Set<ImageReferenceEntity> currentReferences, Set<ImageReferenceEntity> previousReferences) {
        Set<ImageReferenceEntity> newReferences = new HashSet<>();

        for (ImageReferenceEntity reference : currentReferences) {
            // Try to remove current references from old references
            if (!previousReferences.removeIf(r -> r.getImageName().equals(reference.getImageName()))) {
                // If not in old reference, it is a new one
                newReferences.add(reference);
            }
        }

        // Set list to only have new references
        currentReferences.clear();
        currentReferences.addAll(newReferences);
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

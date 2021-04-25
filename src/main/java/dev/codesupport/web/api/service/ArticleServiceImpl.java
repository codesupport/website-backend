package dev.codesupport.web.api.service;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.api.data.entity.*;
import dev.codesupport.web.api.data.repository.*;
import dev.codesupport.web.common.exception.DuplicateEntryException;
import dev.codesupport.web.common.service.service.CrudAuditableOperations;
import dev.codesupport.web.common.service.service.CrudLogic;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.ImageReferenceScanner;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    private final CrudAuditableOperations<Long, ArticleEntity, Article, Long> articleCrudOperations;
    private final CrudOperations<ArticleRevisionEntity, ArticleRevision, Long> revisionCrudOperations;
    private final ArticleRepository articleRepository;
    private final ImageReferenceRepository imageReferenceRepository;
    private final ArticleRevisionRepository articleRevisionRepository;
    private final ImageReferenceScanner imageReferenceScanner;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            ArticleRevisionRepository articleRevisionRepository,
            ImageReferenceRepository imageReferenceRepository,
            TagSetToTagsRepository tagSetToTagsRepository,
            TagSetRepository tagSetRepository,
            TagRepository tagRepository,
            ImageReferenceScanner imageReferenceScanner
    ) {
        articleCrudOperations = new CrudAuditableOperations<>(articleRepository, ArticleEntity.class, Article.class);
        articleCrudOperations.setCrudLogic(new ArticleCrudLogic(articleRepository));
        revisionCrudOperations = new CrudOperations<>(articleRevisionRepository, ArticleRevisionEntity.class, ArticleRevision.class);
        revisionCrudOperations.setCrudLogic(new ArticleRevisionCrudLogic(tagRepository, tagSetRepository, tagSetToTagsRepository));
        this.articleRepository = articleRepository;
        this.articleRevisionRepository = articleRevisionRepository;
        this.imageReferenceRepository = imageReferenceRepository;
        this.imageReferenceScanner = imageReferenceScanner;
    }

    @Override
    public List<Article> findAllArticles(boolean publishedOnly, Long creatorId) {
        if (publishedOnly && creatorId != -1) {
            return articleRepository
                    .findAllByAuditEntity_CreatedBy_IdAndRevisionIdNotNull(creatorId)
                    .stream()
                    .map(this::getArticleWithRevision)
                    .collect(Collectors.toList());
        }

        if (publishedOnly) {
            return articleRepository
                    .findAllByRevisionIdNotNull()
                    .stream()
                    .map(this::getArticleWithRevision)
                    .collect(Collectors.toList());
        }

        if (creatorId != -1) {
            return articleRepository
                    .findAllByAuditEntity_CreatedBy_Id(creatorId)
                    .stream()
                    .map(this::getArticleWithRevision)
                    .collect(Collectors.toList());
        }

        // For each article entity, get an article DTO with the current revision nested inside
        return articleRepository
                .findAll()
                .stream()
                .map(this::getArticleWithRevision)
                .collect(Collectors.toList());
    }

    @Override
    public Article getArticleById(Long id) {
        return getArticleWithRevision(articleRepository.getById(id));
    }

    @VisibleForTesting
    Article getArticleWithRevision(ArticleEntity entity) {
        Article article = MappingUtils.convertToType(entity, Article.class);

        if (entity.getRevisionId() != null) {
            article.setRevision(revisionCrudOperations.getById(entity.getRevisionId()));
        }

        return article;
    }

    @Override
    public Article createArticle(Article article) {
        return articleCrudOperations.createEntity(article);
    }

    @Override
    public Article updateArticle(Article article) {
        Article updatedArticle = articleCrudOperations.updateEntity(article);

        return getArticleById(updatedArticle.getId());
    }

    @Override
    public VoidMethodResponse deleteArticle(Article article) {
        return new VoidMethodResponse("Not implemented", 0);
    }

    @Override
    public List<ArticleRevision> findAllArticleRevisionsByArticleId(Long id) {
        List<ArticleRevisionEntity> entities = articleRevisionRepository.findAllByArticleId(id);

        return MappingUtils.convertToType(entities, ArticleRevision.class);
    }

    @Override
    public ArticleRevision getArticleRevisionById(Long id) {
        return revisionCrudOperations.getById(id);
    }

    @Override
    @Transactional
    public ArticleRevision createArticleRevision(ArticleRevision articleRevision) {
        ArticleRevision createdRevision = revisionCrudOperations.createEntity(articleRevision);
        saveImageReferences(createdRevision);
        return createdRevision;
    }

    @Override
    public VoidMethodResponse deleteArticleRevision(ArticleRevision articleRevision) {
        return new VoidMethodResponse("Not implemented", 0);
    }

    @VisibleForTesting
    void saveImageReferences(ArticleRevision articleRevision) {
        Set<ImageReferenceEntity> newReferences = scanForImageReferences(articleRevision);
        imageReferenceRepository.saveAll(newReferences);
    }

    @VisibleForTesting
    Set<ImageReferenceEntity> scanForImageReferences(ArticleRevision articleRevision) {
        Set<String> fileNames = imageReferenceScanner.scan(articleRevision.getContent());

        return fileNames.stream().map(fileName -> {
            ImageReferenceEntity imageReferenceEntity = new ImageReferenceEntity();
            imageReferenceEntity.setRevisionId(articleRevision.getId());
            imageReferenceEntity.setImageName(fileName);
            return imageReferenceEntity;
        }).collect(Collectors.toSet());
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class ArticleCrudLogic extends CrudLogic<ArticleEntity, Article> {

        private final ArticleRepository articleRepository;

        @Override
        public void preCreateLogic(Article article) {
            String titleId = getTitleIdFromTitle(article.getTitle());
            if (articleRepository.existsByTitleId(titleId)) {
                throw new DuplicateEntryException("Article with similar name already exists.");
            } else {
                article.setTitleId(titleId);
            }
        }

        @Override
        public void preSaveLogic(ArticleEntity articleEntity, Article article) {
            if (article.getRevision() != null) {
                articleEntity.setRevisionId(article.getRevision().getId());
            }
        }

        @VisibleForTesting
        String getTitleIdFromTitle(String title) {
            return title.trim()
                    .toLowerCase()
                    .replaceAll("[^\\w ]", "")
                    .replaceAll("\\s", "-");
        }

    }

    static class ArticleRevisionCrudLogic extends CrudLogic<ArticleRevisionEntity, ArticleRevision> {

        private final TagRepository tagRepository;
        private final TagSetRepository tagSetRepository;
        private final TagSetToTagsRepository tagSetToTagsRepository;

        public ArticleRevisionCrudLogic(
                TagRepository tagRepository,
                TagSetRepository tagSetRepository,
                TagSetToTagsRepository tagSetToTagsRepository
        ) {
            this.tagRepository = tagRepository;
            this.tagSetRepository = tagSetRepository;
            this.tagSetToTagsRepository = tagSetToTagsRepository;
        }

        @Override
        public void preGetLogic(ArticleRevisionEntity articleRevisionEntity, ArticleRevision articleRevision) {
            Set<TagSetToTagEntity> xMaps = tagSetToTagsRepository.findAllByTagSetId(articleRevisionEntity.getTagSetId());
            Set<TagEntity> tags = xMaps.stream().map(TagSetToTagEntity::getTag).collect(Collectors.toSet());
            TagSet tagSet = new TagSet();
            tagSet.setTags(MappingUtils.convertToType(tags, Tag.class));
            articleRevision.setTagSet(tagSet);
        }

        @Override
        public void preSaveLogic(ArticleRevisionEntity articleRevisionEntity, ArticleRevision articleRevision) {
            Set<TagEntity> adjustedTagEntities = compileNewAndExistingTags(articleRevision);

            createTagReferences(articleRevisionEntity, adjustedTagEntities);
        }

        @VisibleForTesting
        Set<TagEntity> compileNewAndExistingTags(ArticleRevision articleRevision) {
            Set<TagEntity> adjustedTagEntities = new HashSet<>();
            Set<String> tagLabels = articleRevision.getTags();

            Set<TagEntity> existingTags = tagRepository.findAllByLabelIn(tagLabels);

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

            return adjustedTagEntities;
        }

        @VisibleForTesting
        void createTagReferences(ArticleRevisionEntity articleRevisionEntity, Set<TagEntity> tagsToSave) {
            Set<TagEntity> createdTagEntities = new HashSet<>(tagRepository.saveAll(tagsToSave));
            TagSetEntity createdTagSetEntity = tagSetRepository.save(new TagSetEntity());
            articleRevisionEntity.setTagSetId(createdTagSetEntity.getId());

            for (TagEntity tag : createdTagEntities) {
                TagSetToTagEntity xMap = new TagSetToTagEntity();
                xMap.setTagSet(createdTagSetEntity);
                xMap.setTag(tag);
                tagSetToTagsRepository.save(xMap);
            }
        }

    }

}

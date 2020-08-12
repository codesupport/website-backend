package dev.codesupport.web.api.service;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.TagEntity;
import dev.codesupport.web.api.data.entity.TagSetEntity;
import dev.codesupport.web.api.data.entity.TagSetToTagEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.TagRepository;
import dev.codesupport.web.api.data.repository.TagSetRepository;
import dev.codesupport.web.api.data.repository.TagSetToTagsRepository;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.Article;
import lombok.extern.slf4j.Slf4j;
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
    private final TagSetToTagsRepository tagSetToTagsRepository;
    private final TagSetRepository tagSetRepository;
    private final TagRepository tagRepository;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            TagSetToTagsRepository tagSetToTagsRepository,
            TagSetRepository tagSetRepository,
            TagRepository tagRepository
    ) {
        crudOperations = new CrudOperations<>(articleRepository, ArticleEntity.class, Article.class);
        crudOperations.setPreSaveEntities(preSaveLogic());
        crudOperations.setPreGetEntities(preGetLogic());
        this.tagSetToTagsRepository = tagSetToTagsRepository;
        this.tagSetRepository = tagSetRepository;
        this.tagRepository = tagRepository;
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

    Consumer<ArticleEntity> preSaveLogic() {
        return (ArticleEntity articleEntity) -> {
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
        };
    }

    Consumer<ArticleEntity> preGetLogic() {
        return (articleEntity) -> {
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
}

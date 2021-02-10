package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.ArticleRevisionEntity;
import dev.codesupport.web.domain.ArticleRevision;

import java.util.Set;

/**
 * This is a test utility used to create objects for unit tests
 */
public class ArticleRevisionBuilder {

    private Long id;
    private Long articleId;
    private String description;
    private String content;
    private Set<String> tags;
    private Long tagSetId;
    private UserBuilder createdBy;
    private Long createdOn;

    private ArticleRevisionBuilder() {

    }

    public static ArticleRevisionBuilder builder() {
        return new ArticleRevisionBuilder();
    }

    public ArticleRevision buildDomain() {
        ArticleRevision domain = new ArticleRevision();
        domain.setId(id);
        domain.setArticleId(articleId);
        domain.setDescription(description);
        domain.setContent(content);
        domain.setTags(tags);
        if (createdBy != null) {
            domain.setCreatedBy(createdBy.buildDomain());
        }
        domain.setCreatedOn(createdOn);
        return domain;
    }

    public ArticleRevisionEntity buildEntity() {
        ArticleRevisionEntity entity = new ArticleRevisionEntity();
        entity.setId(id);
        entity.setArticleId(articleId);
        entity.setDescription(description);
        entity.setContent(content);
        entity.setTagSetId(tagSetId);
        if (createdBy != null) {
            entity.setCreatedBy(createdBy.buildEntity());
        }
        entity.setCreatedOn(createdOn);
        return entity;
    }

    public ArticleRevisionBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ArticleRevisionBuilder articleId(Long articleId) {
        this.articleId = articleId;
        return this;
    }

    public ArticleRevisionBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ArticleRevisionBuilder content(String content) {
        this.content = content;
        return this;
    }

    public ArticleRevisionBuilder tags(Set<String> tags) {
        this.tags = tags;
        return this;
    }

    public ArticleRevisionBuilder tagSetId(Long tagSetId) {
        this.tagSetId = tagSetId;
        return this;
    }

    public ArticleRevisionBuilder createdBy(UserBuilder createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ArticleRevisionBuilder createdOn(Long createdOn) {
        this.createdOn = createdOn;
        return this;
    }

}

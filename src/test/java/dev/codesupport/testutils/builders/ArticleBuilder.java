package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.domain.Article;

import java.util.List;

public class ArticleBuilder {

    private Long id;
    private String articleCode;
    private String title;
    private String description;
    private String content;
    private List<String> tags;
    private UserBuilder createdBy;
    private Long createdOn;
    private UserBuilder updatedBy;
    private Long updatedOn;

    private ArticleBuilder() {

    }

    public static ArticleBuilder builder() {
        return new ArticleBuilder();
    }

    public Article buildDomain() {
        Article domain = new Article();
        domain.setId(id);
        domain.setArticleCode(articleCode);
        domain.setTitle(title);
        domain.setDescription(description);
        domain.setContent(content);
        domain.setTags(tags);
        if (createdBy != null)
            domain.setCreatedBy(createdBy.buildDomain());
        domain.setCreatedOn(createdOn);
        if (updatedBy != null)
            domain.setUpdatedBy(updatedBy.buildDomain());
        domain.setUpdatedOn(updatedOn);
        return domain;
    }

    public ArticleEntity buildEntity() {
        ArticleEntity entity = new ArticleEntity();
        entity.setId(id);
        entity.setArticleCode(articleCode);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setContent(content);
        if (createdBy != null)
            entity.setCreatedBy(createdBy.buildEntity());
        entity.setCreatedOn(createdOn);
        if (updatedBy != null)
            entity.setUpdatedBy(updatedBy.buildEntity());
        entity.setUpdatedOn(updatedOn);
        return entity;
    }

    public ArticleBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ArticleBuilder articleCode(String articleCode) {
        this.articleCode = articleCode;
        return this;
    }

    public ArticleBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ArticleBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ArticleBuilder content(String content) {
        this.content = content;
        return this;
    }

    public ArticleBuilder tags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public ArticleBuilder createdBy(UserBuilder createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ArticleBuilder createdOn(Long createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public ArticleBuilder updatedBy(UserBuilder updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public ArticleBuilder updatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }
}

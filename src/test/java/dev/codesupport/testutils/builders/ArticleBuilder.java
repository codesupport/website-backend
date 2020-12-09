package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.User;

import java.util.List;

public class ArticleBuilder {

    private Long id;
    private String articleCode;
    private String title;
    private String description;
    private String content;
    private List<String> tags;
    private User createdBy;
    private Long createdOn;
    private User updatedBy;
    private Long updatedOn;

    private ArticleBuilder() {

    }

    public static ArticleBuilder builder() {
        return new ArticleBuilder();
    }

    public Article buildDomain() {
        Article domain = new Article();
        domain.setId(id);
        return domain;
    }

    public ArticleEntity buildEntity() {
        ArticleEntity entity = new ArticleEntity();
        entity.setId(id);
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

    public ArticleBuilder createdBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ArticleBuilder createdOn(Long createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public ArticleBuilder updatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public ArticleBuilder updatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }
}

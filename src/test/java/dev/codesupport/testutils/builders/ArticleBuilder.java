package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.domain.Article;

/**
 * This is a test utility used to create objects for unit tests
 */
public class ArticleBuilder {

    private Long id;
    private String title;
    private ArticleRevisionBuilder revision;
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
        domain.setTitle(title);
        if (revision != null) {
            domain.setRevision(revision.buildDomain());
        }
        if (createdBy != null) {
            domain.setCreatedBy(createdBy.buildDomain());
        }
        domain.setCreatedOn(createdOn);
        if (updatedBy != null) {
            domain.setUpdatedBy(updatedBy.buildDomain());
        }
        domain.setUpdatedOn(updatedOn);
        return domain;
    }

    public ArticleEntity buildEntity() {
        ArticleEntity entity = new ArticleEntity();
        entity.setId(id);
        if (revision != null) {
            entity.setRevisionId(revision.buildEntity().getId());
        }
        entity.setTitle(title);
        if (createdBy != null) {
            entity.getAuditEntity().setCreatedBy(createdBy.buildEntity());
        }
        entity.getAuditEntity().setCreatedOn(createdOn);
        if (updatedBy != null) {
            entity.getAuditEntity().setUpdatedBy(updatedBy.buildEntity());
        }
        entity.getAuditEntity().setUpdatedOn(updatedOn);
        return entity;
    }

    public ArticleBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ArticleBuilder revision(ArticleRevisionBuilder revision) {
        this.revision = revision;
        return this;
    }

    public ArticleBuilder title(String title) {
        this.title = title;
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

package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.PublishedArticleEntity;
import dev.codesupport.web.domain.PublishedArticle;

public class PublishedArticleBuilder {

    private Long id;
    private ArticleBuilder article;
    private String articleCode;
    private boolean published;

    private PublishedArticleBuilder() {

    }

    public static PublishedArticleBuilder builder() {
        return new PublishedArticleBuilder();
    }

    public PublishedArticle buildDomain() {
        PublishedArticle domain = new PublishedArticle();
        domain.setId(id);
        domain.setArticle(article.buildDomain());
        domain.setArticleCode(articleCode);
        domain.setPublished(published);
        return domain;
    }

    public PublishedArticleEntity buildEntity() {
        PublishedArticleEntity entity = new PublishedArticleEntity();
        entity.setId(id);
        entity.setArticleId(article.buildEntity().getId());
        entity.setArticleCode(articleCode);
        entity.setPublished(published);
        return entity;
    }

    public PublishedArticleBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PublishedArticleBuilder article(ArticleBuilder article) {
        this.article = article;
        return this;
    }

    public PublishedArticleBuilder articleCode(String articleCode) {
        this.articleCode = articleCode;
        return this;
    }

    public PublishedArticleBuilder published(boolean published) {
        this.published = published;
        return this;
    }

}

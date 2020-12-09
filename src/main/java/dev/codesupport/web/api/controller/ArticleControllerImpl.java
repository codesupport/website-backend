package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.ArticleService;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleControllerImpl implements ArticleController {

    private final ArticleService service;

    @Autowired
    public ArticleControllerImpl(ArticleService service) {
        this.service = service;
    }

    @Override
    public List<PublishedArticle> findAllArticles(boolean publishedonly) {
        return service.findAllArticles(publishedonly);
    }

    @Override
    public List<Article> findAllArticleRevisionsById(Long id) {
        return service.findAllArticleRevisionsById(id);
    }

    @Override
    public PublishedArticle getArticleById(Long id) {
        return service.getArticleById(id);
    }

    @Override
    public PublishedArticle createArticle(Article article) {
        return service.createArticle(article);
    }

    @Override
    public Article updateArticle(Article article) {
        return service.updateArticle(article);
    }

    @Override
    public VoidMethodResponse deleteArticle(Article article) {
        return service.deleteArticle(article);
    }

    @Override
    public VoidMethodResponse publishArticle(PublishedArticle publishedArticle) {
        return service.publishArticle(publishedArticle);
    }
}

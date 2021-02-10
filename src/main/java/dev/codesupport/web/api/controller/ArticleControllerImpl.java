package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.ArticleService;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.ArticleRevision;
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
    public List<Article> findAllArticles(boolean publishedonly) {
        return service.findAllArticles(publishedonly);
    }

    @Override
    public Article getArticleById(Long id) {
        return service.getArticleById(id);
    }

    @Override
    public Article createArticle(Article article) {
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
    public List<ArticleRevision> findAllArticleRevisionsByArticleId(Long id) {
        return service.findAllArticleRevisionsByArticleId(id);
    }

    @Override
    public ArticleRevision getArticleRevisionById(Long id) {
        return service.getArticleRevisionById(id);
    }

    @Override
    public ArticleRevision createArticleRevision(ArticleRevision articleRevision) {
        return service.createArticleRevision(articleRevision);
    }

    @Override
    public VoidMethodResponse deleteArticleRevision(ArticleRevision articleRevision) {
        return service.deleteArticleRevision(articleRevision);
    }

}

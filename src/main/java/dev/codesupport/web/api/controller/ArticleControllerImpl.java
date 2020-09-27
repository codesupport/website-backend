package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.ArticleService;
import dev.codesupport.web.domain.Article;
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
    public List<Article> findAllArticles() {
        return service.findAllArticles();
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
    public Article deleteArticle(Article article) {
        return service.deleteArticle(article);
    }
}

package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.Article;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ArticleService {

    List<Article> findAllArticles();

    Article getArticleById(Long id);

    @PreAuthorize("hasPermission(#article, 'create')")
    Article createArticle(Article article);

    @PreAuthorize("hasPermission(#article, 'update')")
    Article updateArticle(Article article);

    @PreAuthorize("hasPermission(#article, 'delete')")
    Article deleteArticle(Article article);

    @PreAuthorize("hasPermission(#article, 'publish')")
    Article publishArticle(Article article);

}

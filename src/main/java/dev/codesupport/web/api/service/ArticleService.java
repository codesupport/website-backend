package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ArticleService {

    List<PublishedArticle> findAllArticles(boolean publishedOnly);

    List<Article> findAllArticleRevisionsById(Long id);

    PublishedArticle getArticleById(Long id);

    @PreAuthorize("hasPermission(#article, 'create')")
    PublishedArticle createArticle(Article article);

    @PreAuthorize("hasPermission(#article, 'update')")
    Article updateArticle(Article article);

    //TODO: Finish implementing
//    @PreAuthorize("hasPermission(#article, 'delete')")
    VoidMethodResponse deleteArticle(Article article);

    @PreAuthorize("hasPermission(#article, 'publish')")
    VoidMethodResponse publishArticle(PublishedArticle publishedArticle);

}

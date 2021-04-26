package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.ArticleRevision;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ArticleService {

    @PreAuthorize("hasPermission(#publishedOnly, 'articles', 'read') || hasPermission(#creatorId, 'articlescreator', 'read')")
    List<Article> findAllArticles(boolean publishedOnly, Long creatorId);

    @PreAuthorize("hasPermission(#id, 'article', 'read')")
    Article getArticleById(Long id);

    @PreAuthorize("hasPermission(#article, 'create')")
    Article createArticle(Article article);

    @PreAuthorize("hasPermission(#article, 'update')")
    Article updateArticle(Article article);

    //TODO: Finish implementing
//    @PreAuthorize("hasPermission(#article, 'delete')")
    VoidMethodResponse deleteArticle(Article article);

    @PreAuthorize("hasPermission(#id, 'articlerevisions', 'read')")
    List<ArticleRevision> findAllArticleRevisionsByArticleId(Long id);

    @PreAuthorize("hasPermission(#id, 'articlerevision', 'read')")
    ArticleRevision getArticleRevisionById(Long id);

    @PreAuthorize("hasPermission(#articleRevision, 'create')")
    ArticleRevision createArticleRevision(ArticleRevision articleRevision);

    //TODO: Finish implementing
//    @PreAuthorize("hasPermission(#articleRevision, 'delete')")
    VoidMethodResponse deleteArticleRevision(ArticleRevision articleRevision);

}

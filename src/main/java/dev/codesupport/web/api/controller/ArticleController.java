package dev.codesupport.web.api.controller;

import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import dev.codesupport.web.domain.VoidMethodResponse;
import dev.codesupport.web.domain.validation.annotation.ArticleConstraint;
import dev.codesupport.web.domain.validation.annotation.AuditableDomainConstraint;
import dev.codesupport.web.domain.validation.annotation.PublishedArticleConstraint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/article/v1")
@Api(value = "Article", description = "REST API for Articles", tags = {"Article"})
@Validated
public interface ArticleController {

    @ApiOperation("Get all Articles")
    @GetMapping("/articles")
    List<PublishedArticle> findAllArticles(@RequestParam(required = false) boolean publishedonly);

    @ApiOperation("Get an Article by id")
    @GetMapping("/articles/{id}")
    PublishedArticle getArticleById(@PathVariable Long id);

    @ApiOperation("Get all Article revisions")
    @GetMapping("/revisions/{id}")
    List<Article> findAllArticleRevisionsById(@PathVariable Long id);

    @ApiOperation("Create an Article")
    @PostMapping("/articles")
    PublishedArticle createArticle(@RequestBody @ArticleConstraint Article article);

    @ApiOperation("Create an Article")
    @PutMapping("/articles")
    Article updateArticle(@RequestBody @ArticleConstraint(requireId = true) Article article);

    @ApiOperation("Create an Article")
    @DeleteMapping("/articles")
    VoidMethodResponse deleteArticle(@RequestBody @AuditableDomainConstraint Article article);

    @ApiOperation("Publish an Article")
    @PostMapping("/publish")
    VoidMethodResponse publishArticle(@RequestBody @PublishedArticleConstraint PublishedArticle publishedArticle);

}

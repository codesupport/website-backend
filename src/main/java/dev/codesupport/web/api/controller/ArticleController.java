package dev.codesupport.web.api.controller;

import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.ArticleRevision;
import dev.codesupport.web.domain.VoidMethodResponse;
import dev.codesupport.web.domain.validation.annotation.ArticleRevisionConstraint;
import dev.codesupport.web.domain.validation.annotation.AuditableDomainConstraint;
import dev.codesupport.web.domain.validation.annotation.ArticleConstraint;
import dev.codesupport.web.domain.validation.annotation.IdentifiableDomainConstraint;
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
    List<Article> findAllArticles(@RequestParam(required = false) boolean publishedonly);

    @ApiOperation("Get an Article by id")
    @GetMapping("/articles/{id}")
    Article getArticleById(@PathVariable Long id);

    @ApiOperation("Create an Article")
    @PostMapping("/articles")
    Article createArticle(@RequestBody @ArticleConstraint(isCreate = true) Article article);

    @ApiOperation("Create an Article")
    @PutMapping("/articles")
    Article updateArticle(@RequestBody @ArticleConstraint Article article);

    @ApiOperation("Create an Article")
    @DeleteMapping("/articles")
    VoidMethodResponse deleteArticle(@RequestBody @AuditableDomainConstraint Article article);

    @ApiOperation("Get all Article revisions")
    @GetMapping("/articles/{id}/revisions")
    List<ArticleRevision> findAllArticleRevisionsByArticleId(@PathVariable Long id);

    @ApiOperation("Get Article Revision by ID")
    @GetMapping("/revisions/{id}")
    ArticleRevision getArticleRevisionById(@PathVariable Long id);

    @ApiOperation("Create Article Revision")
    @PostMapping("/revisions")
    ArticleRevision createArticleRevision(@RequestBody @ArticleRevisionConstraint ArticleRevision articleRevision);

    @ApiOperation("Delete Article Revision")
    @DeleteMapping("/revisions")
    VoidMethodResponse deleteArticleRevision(@RequestBody @IdentifiableDomainConstraint ArticleRevision articleRevision);

}

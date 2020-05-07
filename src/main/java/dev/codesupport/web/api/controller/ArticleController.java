package dev.codesupport.web.api.controller;

import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.validation.annotation.ArticleConstraint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/article/v1")
@Api(value = "Article", description = "REST API for Articles", tags = {"Article"})
@Validated
public interface ArticleController {

    @ApiOperation("Get all Articles")
    @GetMapping("/articles")
    List<Article> findAllArticles();

    @ApiOperation("Get an Article by id")
    @GetMapping("/articles/{id}")
    Article getArticleById(@PathVariable Long id);

    @ApiOperation("Create an Article")
    @PostMapping("/articles")
    Article createArticle(@RequestBody @ArticleConstraint Article article);

}

package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.ArticleService;
import dev.codesupport.web.domain.Article;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ArticleControllerImplTest {

    private static ArticleControllerImpl controller;

    private static ArticleService mockService;

    @BeforeClass
    public static void init() {
        mockService = mock(ArticleService.class);

        controller = new ArticleControllerImpl(mockService);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockService
        );
    }

    @Test
    public void shouldReturnCorrectResultsForFindAllArticles() {
        List<Article> expected = Collections.singletonList(mock(Article.class));

        doReturn(expected)
                .when(mockService)
                .findAllArticles();

        List<Article> actual = controller.findAllArticles();

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForGetArticleById() {
        Article expected = mock(Article.class);

        doReturn(expected)
                .when(mockService)
                .getArticleById(5L);

        Article actual = controller.getArticleById(5L);

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResponseForCreateArticle() {
        Article submit = mock(Article.class);
        Article expected = mock(Article.class);

        doReturn(expected)
                .when(mockService)
                .createArticle(submit);

        Article actual = controller.createArticle(submit);

        assertSame(expected, actual);
    }

}

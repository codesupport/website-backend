package dev.codesupport.web.api.service;

import dev.codesupport.testutils.builders.ArticleBuilder;
import dev.codesupport.testutils.builders.PublishedArticleBuilder;
import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.PublishedArticleEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.PublishedArticleRepository;
import dev.codesupport.web.api.data.repository.TagRepository;
import dev.codesupport.web.api.data.repository.TagSetRepository;
import dev.codesupport.web.api.data.repository.TagSetToTagsRepository;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ArticleServiceImplTest {

    private static PublishedArticleRepository mockPublishedArticleRepository;
    private static ArticleRepository mockArticleRepository;
    private static TagSetToTagsRepository mockTagSetToTagsRepository;
    private static TagSetRepository mockTagSetRepository;
    private static TagRepository mockTagRepository;
    private static CrudOperations<ArticleEntity, Article, Long> mockCrudOperations;

    private static ArticleServiceImpl serviceSpy;

    @BeforeClass
    public static void init() {
        mockPublishedArticleRepository = mock(PublishedArticleRepository.class);
        mockArticleRepository = mock(ArticleRepository.class);
        mockTagSetToTagsRepository = mock(TagSetToTagsRepository.class);
        mockTagSetRepository = mock(TagSetRepository.class);
        mockTagRepository = mock(TagRepository.class);
        mockCrudOperations = mock(CrudOperations.class);

        serviceSpy = spy(
                new ArticleServiceImpl(
                        mockPublishedArticleRepository,
                        mockArticleRepository,
                        mockTagSetToTagsRepository,
                        mockTagSetRepository,
                        mockTagRepository
                )
        );

        ReflectionTestUtils.setField(serviceSpy, "crudOperations", mockCrudOperations);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockPublishedArticleRepository,
                mockArticleRepository,
                mockTagSetToTagsRepository,
                mockTagSetRepository,
                mockTagRepository,
                mockCrudOperations,
                serviceSpy
        );
    }

    @Test
    public void shouldFindAllArticlesIfPublishedOnly() {
        PublishedArticleBuilder builder = PublishedArticleBuilder
                .builder()
                .id(15L)
                .article(
                        ArticleBuilder.builder()
                                .id(3L)
                )
                .articleCode("code")
                .published(true);

        List<PublishedArticleEntity> entities = Collections.singletonList(
                builder.buildEntity()
        );

        doReturn(entities)
                .when(mockPublishedArticleRepository)
                .findAllByPublishedIsTrue();

        doReturn(null)
                .when(mockPublishedArticleRepository)
                .findAll();

        List<PublishedArticle> expected = Collections.singletonList(
                builder.buildDomain()
        );

        doReturn(expected.get(0).getArticle())
                .when(mockCrudOperations)
                .getById(expected.get(0).getArticle().getId());

        List<PublishedArticle> actual = serviceSpy.findAllArticles(true);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindAllArticles() {
        PublishedArticleBuilder builder = PublishedArticleBuilder
                .builder()
                .id(15L)
                .article(
                        ArticleBuilder.builder()
                                .id(3L)
                )
                .articleCode("code")
                .published(true);

        List<PublishedArticleEntity> entities = Collections.singletonList(
                builder.buildEntity()
        );

        doReturn(null)
                .when(mockPublishedArticleRepository)
                .findAllByPublishedIsTrue();

        doReturn(entities)
                .when(mockPublishedArticleRepository)
                .findAll();

        List<PublishedArticle> expected = Collections.singletonList(
                builder.buildDomain()
        );

        doReturn(expected.get(0).getArticle())
                .when(mockCrudOperations)
                .getById(expected.get(0).getArticle().getId());

        List<PublishedArticle> actual = serviceSpy.findAllArticles(false);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindAllArticleRevisionsById() {
        Long id = 15L;
        String articleCode = "code";

        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(7L)
                .articleCode(articleCode);

        PublishedArticleBuilder publishedArticleBuilder = PublishedArticleBuilder.builder()
                .id(id)
                .article(articleBuilder)
                .articleCode(articleCode)
                .published(false);

        doReturn(publishedArticleBuilder.buildEntity())
                .when(mockPublishedArticleRepository)
                .getById(id);

        doReturn(Collections.singletonList(articleBuilder.buildEntity()))
                .when(mockArticleRepository)
                .findAllByArticleCode(articleCode);

        List<Article> expected = Collections.singletonList(articleBuilder.buildDomain());

        List<Article> actual = serviceSpy.findAllArticleRevisionsById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetArticleById() {
        Long id = 15L;
        String articleCode = "code";

        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(7L)
                .articleCode(articleCode);

        PublishedArticleBuilder publishedArticleBuilder = PublishedArticleBuilder.builder()
                .id(id)
                .article(articleBuilder)
                .articleCode(articleCode)
                .published(false);

        doReturn(publishedArticleBuilder.buildEntity())
                .when(mockPublishedArticleRepository)
                .getById(id);

        doReturn(articleBuilder.buildDomain())
                .when(mockCrudOperations)
                .getById(articleBuilder.buildDomain().getId());

        PublishedArticle expected = publishedArticleBuilder.buildDomain();

        PublishedArticle actual = serviceSpy.getArticleById(id);

        assertEquals(expected, actual);
    }

}

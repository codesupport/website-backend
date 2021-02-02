package dev.codesupport.web.api.service;

import dev.codesupport.testutils.builders.ArticleBuilder;
import dev.codesupport.testutils.builders.ImageReferenceBuilder;
import dev.codesupport.testutils.builders.PublishedArticleBuilder;
import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.ImageReferenceEntity;
import dev.codesupport.web.api.data.entity.PublishedArticleEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.ImageReferenceRepository;
import dev.codesupport.web.api.data.repository.PublishedArticleRepository;
import dev.codesupport.web.api.data.repository.TagRepository;
import dev.codesupport.web.api.data.repository.TagSetRepository;
import dev.codesupport.web.api.data.repository.TagSetToTagsRepository;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.ImageReferenceScanner;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ArticleServiceImplTest {

    private static PublishedArticleRepository mockPublishedArticleRepository;
    private static ArticleRepository mockArticleRepository;
    private static ImageReferenceRepository mockImageReferenceRepository;
    private static TagSetToTagsRepository mockTagSetToTagsRepository;
    private static TagSetRepository mockTagSetRepository;
    private static TagRepository mockTagRepository;
    private static ImageReferenceScanner mockImageReferenceScanner;
    private static CrudOperations<ArticleEntity, Article, Long> mockCrudOperations;

    private static ArticleServiceImpl serviceSpy;

    @BeforeClass
    public static void init() {
        mockPublishedArticleRepository = mock(PublishedArticleRepository.class);
        mockArticleRepository = mock(ArticleRepository.class);
        mockImageReferenceRepository = mock(ImageReferenceRepository.class);
        mockTagSetToTagsRepository = mock(TagSetToTagsRepository.class);
        mockTagSetRepository = mock(TagSetRepository.class);
        mockTagRepository = mock(TagRepository.class);
        mockImageReferenceScanner = mock(ImageReferenceScanner.class);
        //unchecked - This is fine for a mock
        //noinspection unchecked
        mockCrudOperations = mock(CrudOperations.class);

        serviceSpy = spy(
                new ArticleServiceImpl(
                        mockPublishedArticleRepository,
                        mockArticleRepository,
                        mockImageReferenceRepository,
                        mockTagSetToTagsRepository,
                        mockTagSetRepository,
                        mockTagRepository,
                        mockImageReferenceScanner
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

    @Test
    public void shouldUpdateImageReferencesOnUpdateArticle() {
        Article article = ArticleBuilder.builder()
                .id(5L)
                .content("hello")
                .buildDomain();

        doNothing()
                .when(serviceSpy)
                .updateImageReferences(any());

        doReturn(null)
                .when(mockCrudOperations)
                .updateEntity(any());

        doReturn(article)
                .when(mockCrudOperations)
                .updateEntity(any());

        serviceSpy.updateArticle(article);

        verify(serviceSpy, times(1))
                .updateImageReferences(article);
    }

    @Test
    public void shouldInvokeUpdateArticleTransaction() {
        Article expected = ArticleBuilder.builder()
                .id(5L)
                .content("hello")
                .buildDomain();

        doNothing()
                .when(serviceSpy)
                .updateArticleTransaction(any());

        doReturn(null)
                .when(mockCrudOperations)
                .getById(anyLong());

        doReturn(expected)
                .when(mockCrudOperations)
                .getById(expected.getId());

        serviceSpy.updateArticle(expected);

        verify(serviceSpy, times(1))
                .updateArticleTransaction(expected);
    }

    @Test
    public void shouldKeepCheckingIfArticleCodeExists() {
        Article article = ArticleBuilder.builder()
                .id(5L)
                .content("hello")
                .buildDomain();

        doReturn(true)
                .doReturn(false)
                .when(mockPublishedArticleRepository)
                .existsByArticleCode(any());

        serviceSpy.generateArticleCode(article);

        verify(mockPublishedArticleRepository, times(2))
                .existsByArticleCode(any());
    }

    @Test
    public void shouldSetArticleCode() {
        Article article = ArticleBuilder.builder()
                .id(5L)
                .content("hello")
                .buildDomain();

        doReturn(true)
                .doReturn(false)
                .when(mockPublishedArticleRepository)
                .existsByArticleCode(any());

        serviceSpy.generateArticleCode(article);

        verify(mockPublishedArticleRepository, times(1))
                .existsByArticleCode(article.getArticleCode());

        assertEquals(20, article.getArticleCode().length());
    }

    @Test
    public void shouldUpdateReferenceData() {
        Article article = ArticleBuilder.builder()
                .id(5L)
                .content("hello")
                .buildDomain();

        Set<ImageReferenceEntity> newReferences = Sets.newHashSet(
                Arrays.asList(
                        ImageReferenceBuilder.builder().imageName("ref1").buildEntity(),
                        ImageReferenceBuilder.builder().imageName("ref2").buildEntity()
                )
        );
        Set<ImageReferenceEntity> oldReferences = Collections.emptySet();

        doReturn(null)
                .when(serviceSpy)
                .scanForImageReferences(any());

        doReturn(newReferences)
                .when(serviceSpy)
                .scanForImageReferences(article);

        doReturn(oldReferences)
                .when(mockImageReferenceRepository)
                .findAllByArticleId(article.getId());

        doNothing()
                .when(serviceSpy)
                .calculateReferenceDelta(anySet(), anySet());

        serviceSpy.updateImageReferences(article);

        verify(serviceSpy, times(1))
                .calculateReferenceDelta(newReferences, oldReferences);
        verify(mockImageReferenceRepository, times(1))
                .deleteAll(oldReferences);
        verify(mockImageReferenceRepository, times(1))
                .saveAll(newReferences);
    }

    @Test
    public void shouldReturnScannedImageReferences() {
        String ref1 = "aba";
        String ref2 = "daba";

        Article article = ArticleBuilder.builder()
                .id(5L)
                .content("hello")
                .buildDomain();

        Set<String> foundReferences = Sets.newHashSet(
                Arrays.asList(ref1, ref2)
        );

        doReturn(foundReferences)
                .when(mockImageReferenceScanner)
                .scan(article.getContent());

        Set<ImageReferenceEntity> expected = Sets.newHashSet(
                Arrays.asList(
                        ImageReferenceBuilder.builder().articleId(article.getId()).imageName(ref1).buildEntity(),
                        ImageReferenceBuilder.builder().articleId(article.getId()).imageName(ref2).buildEntity()
                )
        );
        Set<ImageReferenceEntity> actual = serviceSpy.scanForImageReferences(article);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFilterOutDeltasForReferences() {
        Long articleId = 5L;

        Set<ImageReferenceEntity> actualA = Sets.newHashSet(
                Arrays.asList(
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("aaa").buildEntity(),
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("bbb").buildEntity(),
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("ccc").buildEntity()
                )
        );
        Set<ImageReferenceEntity> actualB = Sets.newHashSet(
                Arrays.asList(
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("eee").buildEntity(),
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("bbb").buildEntity(),
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("fff").buildEntity(),
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("ggg").buildEntity(),
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("ccc").buildEntity()
                )
        );

        serviceSpy.calculateReferenceDelta(actualA, actualB);

        Set<ImageReferenceEntity> expectedA = Sets.newHashSet(
                Collections.singletonList(
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("aaa").buildEntity()
                )
        );
        Set<ImageReferenceEntity> expectedB = Sets.newHashSet(
                Arrays.asList(
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("eee").buildEntity(),
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("fff").buildEntity(),
                        ImageReferenceBuilder.builder().articleId(articleId).imageName("ggg").buildEntity()
                )
        );

        assertEquals(expectedA, actualA);
        assertEquals(expectedB, actualB);
    }

}

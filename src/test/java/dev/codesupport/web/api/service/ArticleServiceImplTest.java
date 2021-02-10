package dev.codesupport.web.api.service;

import com.google.common.collect.Sets;
import dev.codesupport.testutils.builders.ArticleBuilder;
import dev.codesupport.testutils.builders.ArticleRevisionBuilder;
import dev.codesupport.testutils.builders.ImageReferenceBuilder;
import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.ArticleRevisionEntity;
import dev.codesupport.web.api.data.entity.ImageReferenceEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.ArticleRevisionRepository;
import dev.codesupport.web.api.data.repository.ImageReferenceRepository;
import dev.codesupport.web.api.data.repository.TagRepository;
import dev.codesupport.web.api.data.repository.TagSetRepository;
import dev.codesupport.web.api.data.repository.TagSetToTagsRepository;
import dev.codesupport.web.common.exception.DuplicateEntryException;
import dev.codesupport.web.common.service.service.CrudAuditableOperations;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.ImageReferenceScanner;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.ArticleRevision;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ArticleServiceImplTest {

    private static ArticleRepository mockArticleRepository;
    private static ArticleRevisionRepository mockArticleRevisionRepository;
    private static ImageReferenceRepository mockImageReferenceRepository;
    private static TagSetToTagsRepository mockTagSetToTagsRepository;
    private static TagSetRepository mockTagSetRepository;
    private static TagRepository mockTagRepository;
    private static ImageReferenceScanner mockImageReferenceScanner;
    private static CrudAuditableOperations<Long, ArticleEntity, Article, Long> mockArticleCrudOperations;
    private static CrudOperations<ArticleRevisionEntity, ArticleRevision, Long> mockArticleRevisionCrudOperations;

    private static ArticleServiceImpl serviceSpy;

    @BeforeClass
    public static void init() {
        mockArticleRepository = mock(ArticleRepository.class);
        mockArticleRevisionRepository = mock(ArticleRevisionRepository.class);
        mockImageReferenceRepository = mock(ImageReferenceRepository.class);
        mockTagSetToTagsRepository = mock(TagSetToTagsRepository.class);
        mockTagSetRepository = mock(TagSetRepository.class);
        mockTagRepository = mock(TagRepository.class);
        mockImageReferenceScanner = mock(ImageReferenceScanner.class);
        //unchecked - This is fine for a mock
        //noinspection unchecked
        mockArticleCrudOperations = mock(CrudAuditableOperations.class);
        mockArticleRevisionCrudOperations = mock(CrudOperations.class);

        serviceSpy = spy(
                new ArticleServiceImpl(
                        mockArticleRepository,
                        mockArticleRevisionRepository,
                        mockImageReferenceRepository,
                        mockTagSetToTagsRepository,
                        mockTagSetRepository,
                        mockTagRepository,
                        mockImageReferenceScanner
                )
        );

        ReflectionTestUtils.setField(serviceSpy, "articleCrudOperations", mockArticleCrudOperations);
        ReflectionTestUtils.setField(serviceSpy, "revisionCrudOperations", mockArticleRevisionCrudOperations);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockArticleRepository,
                mockArticleRevisionRepository,
                mockTagSetToTagsRepository,
                mockTagSetRepository,
                mockTagRepository,
                mockArticleCrudOperations,
                mockArticleRevisionCrudOperations,
                serviceSpy
        );
    }

    @Test
    public void shouldGetAllArticles() {
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title")
                .revision(
                        ArticleRevisionBuilder.builder()
                        .id(3L)
                        .description("some description")
                        .content("Some content")
                );

        doReturn(null)
                .when(mockArticleRepository)
                .findAllByRevisionIdNotNull();

        doReturn(Collections.singletonList(articleBuilder.buildEntity()))
                .when(mockArticleRepository)
                .findAll();

        doReturn(articleBuilder.buildDomain())
                .when(serviceSpy)
                .getArticleWithRevision(articleBuilder.buildEntity());

        List<Article> expected = Collections.singletonList(articleBuilder.buildDomain());
        List<Article> actual = serviceSpy.findAllArticles(false);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetAllPublishedArticlesOnly() {
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title")
                .revision(
                        ArticleRevisionBuilder.builder()
                                .id(3L)
                                .description("some description")
                                .content("Some content")
                );

        doReturn(Collections.singletonList(articleBuilder.buildEntity()))
                .when(mockArticleRepository)
                .findAllByRevisionIdNotNull();

        doReturn(null)
                .when(mockArticleRepository)
                .findAll();

        doReturn(articleBuilder.buildDomain())
                .when(serviceSpy)
                .getArticleWithRevision(articleBuilder.buildEntity());

        List<Article> expected = Collections.singletonList(articleBuilder.buildDomain());
        List<Article> actual = serviceSpy.findAllArticles(true);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetArticleById() {
        Long id = 5L;

        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(id)
                .title("Some title")
                .revision(
                        ArticleRevisionBuilder.builder()
                                .id(3L)
                                .description("some description")
                                .content("Some content")
                );

        doReturn(articleBuilder.buildEntity())
                .when(mockArticleRepository)
                .getById(id);

        doReturn(articleBuilder.buildDomain())
                .when(serviceSpy)
                .getArticleWithRevision(articleBuilder.buildEntity());

        Article expected = articleBuilder.buildDomain();
        Article actual = serviceSpy.getArticleById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetArticleWithRevision() {
        Long revisionId = 3L;
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title")
                .revision(
                        ArticleRevisionBuilder.builder()
                                .id(revisionId)
                                .description("some description")
                                .content("Some content")
                );

        doReturn(articleBuilder.buildDomain().getRevision())
                .when(mockArticleRevisionCrudOperations)
                .getById(revisionId);

        Article expected = articleBuilder.buildDomain();
        Article actual = serviceSpy.getArticleWithRevision(articleBuilder.buildEntity());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetArticleWithRevisionIfNoRevisionId() {
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title");

        Article expected = articleBuilder.buildDomain();
        Article actual = serviceSpy.getArticleWithRevision(articleBuilder.buildEntity());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateArticle() {
        String title = "Some title";
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title(title)
                .revision(null);

        doReturn(false)
                .when(mockArticleRepository)
                .existsByTitle(title);

        doReturn(articleBuilder.buildDomain())
                .when(mockArticleCrudOperations)
                .createEntity(articleBuilder.buildDomain());

        Article expected = articleBuilder.buildDomain();
        Article actual = serviceSpy.createArticle(articleBuilder.buildDomain());

        assertEquals(expected, actual);
    }

    @Test(expected = DuplicateEntryException.class)
    public void shouldThrowDuplicateEntryExceptionOnCreateArticle() {
        String title = "Some title";
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title(title)
                .revision(null);

        doReturn(true)
                .when(mockArticleRepository)
                .existsByTitle(title);

        serviceSpy.createArticle(articleBuilder.buildDomain());
    }

    @Test
    public void shouldUpdateArticle() {
        Long id = 5L;
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(id)
                .title("Some title")
                .revision(null);

        doReturn(articleBuilder.buildDomain())
                .when(mockArticleCrudOperations)
                .updateEntity(articleBuilder.buildDomain());

        doReturn(articleBuilder.buildDomain())
                .when(serviceSpy)
                .getArticleById(id);

        Article expected = articleBuilder.buildDomain();
        Article actual = serviceSpy.updateArticle(articleBuilder.buildDomain());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteArticle() {
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title")
                .revision(null);

        VoidMethodResponse expected = new VoidMethodResponse("Not implemented", 0);
        VoidMethodResponse actual = serviceSpy.deleteArticle(articleBuilder.buildDomain());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetAllArticleRevisionsByArticleId() {
        Long articleId = 2L;
        ArticleRevisionBuilder revisionBuilder = ArticleRevisionBuilder.builder()
                .id(5L)
                .articleId(articleId)
                .description("Some description")
                .content("Some content")
                .tags(new HashSet<>());

        doReturn(Collections.singletonList(revisionBuilder.buildDomain()))
                .when(mockArticleRevisionRepository)
                .findAllByArticleId(articleId);

        List<ArticleRevision> expected = Collections.singletonList(revisionBuilder.buildDomain());
        List<ArticleRevision> actual = serviceSpy.findAllArticleRevisionsByArticleId(articleId);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetArticleRevisionById() {
        Long id = 5L;
        ArticleRevisionBuilder revisionBuilder = ArticleRevisionBuilder.builder()
                .id(id)
                .articleId(3L)
                .description("Some description")
                .content("Some content")
                .tags(new HashSet<>());

        doReturn(revisionBuilder.buildDomain())
                .when(mockArticleRevisionCrudOperations)
                .getById(id);

        ArticleRevision expected = revisionBuilder.buildDomain();
        ArticleRevision actual = serviceSpy.getArticleRevisionById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateArticleRevision() {
        ArticleRevisionBuilder revisionBuilder = ArticleRevisionBuilder.builder()
                .id(5L)
                .articleId(3L)
                .description("Some description")
                .content("Some content")
                .tags(new HashSet<>());

        doNothing()
                .when(serviceSpy)
                .saveImageReferences(any());

        doReturn(revisionBuilder.buildDomain())
                .when(mockArticleRevisionCrudOperations)
                .createEntity(revisionBuilder.buildDomain());

        ArticleRevision expected = revisionBuilder.buildDomain();
        ArticleRevision actual = serviceSpy.createArticleRevision(revisionBuilder.buildDomain());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldInvokeSaveImageReferencesOnCreateArticleRevision() {
        ArticleRevisionBuilder revisionBuilder = ArticleRevisionBuilder.builder()
                .id(5L)
                .articleId(3L)
                .description("Some description")
                .content("Some content")
                .tags(new HashSet<>());

        doNothing()
                .when(serviceSpy)
                .saveImageReferences(any());

        doReturn(revisionBuilder.buildDomain())
                .when(mockArticleRevisionCrudOperations)
                .createEntity(revisionBuilder.buildDomain());

        serviceSpy.createArticleRevision(revisionBuilder.buildDomain());

        verify(serviceSpy, times(1))
                .saveImageReferences(revisionBuilder.buildDomain());
    }

    @Test
    public void shouldDeleteArticleRevision() {
        ArticleRevisionBuilder revisionBuilder = ArticleRevisionBuilder.builder()
                .id(5L)
                .articleId(3L)
                .description("Some description")
                .content("Some content")
                .tags(new HashSet<>());

        VoidMethodResponse expected = new VoidMethodResponse("Not implemented", 0);
        VoidMethodResponse actual = serviceSpy.deleteArticleRevision(revisionBuilder.buildDomain());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSaveImageReferences() {
        ArticleRevisionBuilder revisionBuilder = ArticleRevisionBuilder.builder()
                .id(5L)
                .articleId(3L)
                .description("Some description")
                .content("Some content")
                .tags(new HashSet<>());

        ImageReferenceBuilder imageReferenceBuilder = ImageReferenceBuilder.builder()
                .id(5L)
                .imageName("Image name")
                .articleId(5L);

        doReturn(Collections.singleton(imageReferenceBuilder.buildEntity()))
                .when(serviceSpy)
                .scanForImageReferences(revisionBuilder.buildDomain());

        doReturn(null)
                .when(mockImageReferenceRepository)
                .saveAll(anySet());

        serviceSpy.saveImageReferences(revisionBuilder.buildDomain());

        verify(mockImageReferenceRepository, times(1))
                .saveAll(Collections.singleton(imageReferenceBuilder.buildEntity()));
    }

    @Test
    public void shouldScanAllImageReferences() {
        Long id = 5L;
        ArticleRevisionBuilder revisionBuilder = ArticleRevisionBuilder.builder()
                .id(id)
                .articleId(3L)
                .description("Some description")
                .content("Some content")
                .tags(new HashSet<>());

        Set<String> stringSet = Sets.newHashSet(
                "String1",
                "String2"
        );

        doReturn(stringSet)
                .when(mockImageReferenceScanner)
                .scan(revisionBuilder.buildDomain().getContent());

        Set<ImageReferenceEntity> expected = stringSet.stream().map(fileName -> {
            ImageReferenceEntity imageReferenceEntity = new ImageReferenceEntity();
            imageReferenceEntity.setRevisionId(id);
            imageReferenceEntity.setImageName(fileName);
            return imageReferenceEntity;
        }).collect(Collectors.toSet());

        Set<ImageReferenceEntity> actual = serviceSpy.scanForImageReferences(revisionBuilder.buildDomain());

        assertEquals(expected, actual);
    }

}

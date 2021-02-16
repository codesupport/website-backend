package dev.codesupport.web.api.service;

import dev.codesupport.testutils.builders.ArticleBuilder;
import dev.codesupport.testutils.builders.ArticleRevisionBuilder;
import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.common.exception.DuplicateEntryException;
import dev.codesupport.web.domain.Article;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ArticleServiceImpl_ArticleCrudLogicTest {

    @Test(expected = DuplicateEntryException.class)
    public void shouldThrowDuplicateEntryExceptionForDuplicateTitle() {
        String titleId = "my-title";

        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("My Title");

        Article actual = articleBuilder.buildDomain();

        ArticleRepository mockArticleRepository = mock(ArticleRepository.class);

        ArticleServiceImpl.ArticleCrudLogic crudLogicSpy = spy(new ArticleServiceImpl.ArticleCrudLogic(mockArticleRepository));

        doReturn(true)
                .when(mockArticleRepository)
                .existsByTitleId(titleId);

        doReturn(titleId)
                .when(crudLogicSpy)
                .getTitleIdFromTitle(actual.getTitle());

        crudLogicSpy.preCreateLogic(actual);
    }

    @Test
    public void shouldSetTitleId() {
        String titleId = "my-title";

        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("My Title");

        Article actual = articleBuilder.buildDomain();

        ArticleRepository mockArticleRepository = mock(ArticleRepository.class);

        ArticleServiceImpl.ArticleCrudLogic crudLogicSpy = spy(new ArticleServiceImpl.ArticleCrudLogic(mockArticleRepository));

        doReturn(false)
                .when(mockArticleRepository)
                .existsByTitleId(titleId);

        doReturn(titleId)
                .when(crudLogicSpy)
                .getTitleIdFromTitle(actual.getTitle());

        crudLogicSpy.preCreateLogic(actual);

        Article expected = articleBuilder.buildDomain();
        expected.setTitleId(titleId);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSetRevisionIdIfExists() {
        Long revisionId = 3L;
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title")
                .revision(
                        ArticleRevisionBuilder.builder().id(revisionId)
                );

        ArticleRepository mockArticleRepository = mock(ArticleRepository.class);

        ArticleServiceImpl.ArticleCrudLogic crudLogic = new ArticleServiceImpl.ArticleCrudLogic(mockArticleRepository);

        ArticleEntity articleEntity = articleBuilder.buildEntity();
        crudLogic.preSaveLogic(articleEntity, articleBuilder.buildDomain());

        assertEquals(revisionId, articleEntity.getRevisionId());
    }

    @Test
    public void shouldNotSetRevisionIdIfDoesNotExists() {
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title");

        ArticleRepository mockArticleRepository = mock(ArticleRepository.class);

        ArticleServiceImpl.ArticleCrudLogic crudLogic = new ArticleServiceImpl.ArticleCrudLogic(mockArticleRepository);

        ArticleEntity articleEntity = articleBuilder.buildEntity();
        crudLogic.preSaveLogic(articleEntity, articleBuilder.buildDomain());

        assertNull(articleEntity.getRevisionId());
    }

    @Test
    public void shouldGenerateTitleIdFromTitle() {
        ArticleRepository mockArticleRepository = mock(ArticleRepository.class);

        ArticleServiceImpl.ArticleCrudLogic crudLogicSpy = new ArticleServiceImpl.ArticleCrudLogic(mockArticleRepository);

        String expected = "my-title-id";
        String actual = crudLogicSpy.getTitleIdFromTitle("   My` \"Title\": 'id'    ");

        assertEquals(expected, actual);
    }

}

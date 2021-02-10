package dev.codesupport.web.api.service;

import dev.codesupport.testutils.builders.ArticleBuilder;
import dev.codesupport.testutils.builders.ArticleRevisionBuilder;
import dev.codesupport.web.api.data.entity.ArticleEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArticleServiceImpl_ArticleCrudLogicTest {

    @Test
    public void shouldSetRevisionIdIfExists() {
        Long revisionId = 3L;
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title")
                .revision(
                        ArticleRevisionBuilder.builder().id(revisionId)
                );

        ArticleServiceImpl.ArticleCrudLogic crudLogic = new ArticleServiceImpl.ArticleCrudLogic();

        ArticleEntity articleEntity = articleBuilder.buildEntity();
        crudLogic.preSaveLogic(articleEntity, articleBuilder.buildDomain());

        assertEquals(revisionId, articleEntity.getRevisionId());
    }

    @Test
    public void shouldNotSetRevisionIdIfDoesNotExists() {
        ArticleBuilder articleBuilder = ArticleBuilder.builder()
                .id(5L)
                .title("Some title");

        ArticleServiceImpl.ArticleCrudLogic crudLogic = new ArticleServiceImpl.ArticleCrudLogic();

        ArticleEntity articleEntity = articleBuilder.buildEntity();
        crudLogic.preSaveLogic(articleEntity, articleBuilder.buildDomain());

        assertNull(articleEntity.getRevisionId());
    }

}

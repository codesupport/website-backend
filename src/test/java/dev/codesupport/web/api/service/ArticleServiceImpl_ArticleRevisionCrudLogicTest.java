package dev.codesupport.web.api.service;

import dev.codesupport.testutils.builders.ArticleRevisionBuilder;
import dev.codesupport.testutils.builders.TagBuilder;
import dev.codesupport.testutils.builders.TagSetBuilder;
import dev.codesupport.testutils.builders.TagSetToTagBuilder;
import dev.codesupport.web.api.data.entity.ArticleRevisionEntity;
import dev.codesupport.web.api.data.entity.TagEntity;
import dev.codesupport.web.api.data.entity.TagSetEntity;
import dev.codesupport.web.api.data.entity.TagSetToTagEntity;
import dev.codesupport.web.api.data.repository.TagRepository;
import dev.codesupport.web.api.data.repository.TagSetRepository;
import dev.codesupport.web.api.data.repository.TagSetToTagsRepository;
import dev.codesupport.web.domain.ArticleRevision;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ArticleServiceImpl_ArticleRevisionCrudLogicTest {

    private static TagRepository mockTagRepository;
    private static TagSetRepository mockTagSetRepository;
    private static TagSetToTagsRepository mockTagSetToTagRepository;
    private static ArticleServiceImpl.ArticleRevisionCrudLogic crudLogicSpy;

    @BeforeClass
    public static void init() {
        mockTagRepository = mock(TagRepository.class);
        mockTagSetRepository = mock(TagSetRepository.class);
        mockTagSetToTagRepository = mock(TagSetToTagsRepository.class);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockTagRepository,
                mockTagSetRepository,
                mockTagSetToTagRepository
        );

        crudLogicSpy = spy(new ArticleServiceImpl.ArticleRevisionCrudLogic(
                mockTagRepository,
                mockTagSetRepository,
                mockTagSetToTagRepository
        ));
    }

    @Test
    public void shouldReturnCorrectTagsForPreGetLogic() {
        Long tagSetId = 5L;

        ArticleRevisionBuilder builder = ArticleRevisionBuilder.builder()
                .id(11L)
                .tags(
                        Sets.newLinkedHashSet(
                                "LabelA",
                                "LabelB"
                        )
                )
                .tagSetId(tagSetId);

        Set<TagSetToTagEntity> tagSetToTagEntities = Sets.newLinkedHashSet(
                TagSetToTagBuilder.builder()
                        .id(1L)
                        .tag(
                                TagBuilder.builder()
                                        .id(3L)
                                        .label("LabelA")
                        )
                        .tagSet(
                                TagSetBuilder.builder().id(tagSetId)
                        )
                        .buildEntity(),
                TagSetToTagBuilder.builder()
                        .id(2L)
                        .tag(
                                TagBuilder.builder()
                                        .id(7L)
                                        .label("LabelB")
                        )
                        .tagSet(
                                TagSetBuilder.builder().id(tagSetId)
                        )
                        .buildEntity()
        );

        doReturn(tagSetToTagEntities)
                .when(mockTagSetToTagRepository)
                .findAllByTagSetId(tagSetId);

        ArticleRevision expected = builder.buildDomain();
        ArticleRevision actual = builder.buildDomain();
        actual.setTags(null);

        crudLogicSpy.preGetLogic(builder.buildEntity(), actual);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldInvokeCreateTagReferencesForPreSaveLogic() {
        ArticleRevisionBuilder builder = ArticleRevisionBuilder.builder()
                .id(11L)
                .tagSetId(17L);

        Set<TagEntity> tagEntities = Collections.singleton(
                TagBuilder.builder()
                        .id(5L)
                        .label("LabelA")
                        .buildEntity()
        );

        doReturn(tagEntities)
                .when(crudLogicSpy)
                .compileNewAndExistingTags(builder.buildDomain());

        doNothing()
                .when(crudLogicSpy)
                .createTagReferences(any(), anySet());

        crudLogicSpy.preSaveLogic(builder.buildEntity(), builder.buildDomain());

        verify(crudLogicSpy, times(1))
                .createTagReferences(builder.buildEntity(), tagEntities);
    }

    @Test
    public void shouldCompileNewAndExistingTags() {
        Long tagSetId = 5L;
        ArticleRevisionBuilder builder = ArticleRevisionBuilder.builder()
                .id(11L)
                .tags(
                        Sets.newLinkedHashSet(
                                "LabelA",
                                "LabelB"
                        )
                )
                .tagSetId(tagSetId);

        Set<TagEntity> tagEntities = Collections.singleton(
                TagBuilder.builder()
                        .id(3L)
                        .label("LabelA")
                        .buildEntity()
        );

        doReturn(tagEntities)
                .when(mockTagRepository)
                .findAllByLabelIn(builder.buildDomain().getTags());

        Set<TagEntity> expected = new HashSet<>(tagEntities);
        expected.add(
                TagBuilder.builder().label("LabelB").buildEntity()
        );

        Set<TagEntity> actual = crudLogicSpy.compileNewAndExistingTags(builder.buildDomain());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateTagReferences() {
        ArticleRevisionEntity articleRevisionEntity = ArticleRevisionBuilder.builder()
                .id(11L)
                .tags(
                        Sets.newLinkedHashSet(
                                "LabelA"
                        )
                )
                .buildEntity();

        TagBuilder tagBuilder = TagBuilder.builder()
                .id(5L)
                .label("LabelA");

        Set<TagEntity> tagsToSave = Collections.singleton(
                tagBuilder.buildEntity()
        );
        tagsToSave.iterator().next().setId(null);

        List<TagEntity> savedTags = Collections.singletonList(
                tagBuilder.buildEntity()
        );

        TagSetBuilder savedTagSetBuilder = TagSetBuilder.builder().id(4L);

        doReturn(savedTags)
                .when(mockTagRepository)
                .saveAll(tagsToSave);

        doReturn(savedTagSetBuilder.buildEntity())
                .when(mockTagSetRepository)
                .save(new TagSetEntity());

        doReturn(null)
                .when(mockTagSetToTagRepository)
                .save(any());

        crudLogicSpy.createTagReferences(articleRevisionEntity, tagsToSave);

        TagSetToTagEntity expectedTagSetToTag = TagSetToTagBuilder.builder()
                .tag(tagBuilder)
                .tagSet(savedTagSetBuilder)
                .buildEntity();

        verify(mockTagSetToTagRepository, times(1))
                .save(any());

        verify(mockTagSetToTagRepository, times(1))
                .save(expectedTagSetToTag);
    }

    @Test
    public void shouldModifyRivisionEntityOnCreateTagReferences() {
        ArticleRevisionBuilder articleRevisionBuilder = ArticleRevisionBuilder.builder()
                .id(11L)
                .tags(
                        Sets.newLinkedHashSet(
                                "LabelA"
                        )
                );

        TagBuilder tagBuilder = TagBuilder.builder()
                .id(5L)
                .label("LabelA");

        Set<TagEntity> tagsToSave = Collections.singleton(
                tagBuilder.buildEntity()
        );
        tagsToSave.iterator().next().setId(null);

        List<TagEntity> savedTags = Collections.singletonList(
                tagBuilder.buildEntity()
        );

        TagSetBuilder savedTagSetBuilder = TagSetBuilder.builder().id(4L);

        doReturn(savedTags)
                .when(mockTagRepository)
                .saveAll(tagsToSave);

        doReturn(savedTagSetBuilder.buildEntity())
                .when(mockTagSetRepository)
                .save(new TagSetEntity());

        doReturn(null)
                .when(mockTagSetToTagRepository)
                .save(any());

        ArticleRevisionEntity actual = articleRevisionBuilder.buildEntity();

        crudLogicSpy.createTagReferences(actual, tagsToSave);

        ArticleRevisionEntity expected = articleRevisionBuilder.buildEntity();
        expected.setTagSetId(savedTagSetBuilder.buildEntity().getId());

        assertEquals(expected, actual);
    }

}


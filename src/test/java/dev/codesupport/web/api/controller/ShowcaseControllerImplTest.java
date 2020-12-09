package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.ShowcaseService;
import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.VoidMethodResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ShowcaseControllerImplTest {

    private static ShowcaseControllerImpl controller;

    private static ShowcaseService mockService;

    @BeforeClass
    public static void init() {
        mockService = mock(ShowcaseService.class);

        controller = new ShowcaseControllerImpl(mockService);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockService
        );
    }

    @Test
    public void shouldReturnCorrectResultsForFindAllShowcases() {
        List<Showcase> expected = Collections.singletonList(mock(Showcase.class));

        doReturn(expected)
                .when(mockService)
                .findAllShowcases();

        List<Showcase> actual = controller.findAllShowcases();

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForFindAllShowcasesByUser() {
        long id = 5;

        List<Showcase> expected = Collections.singletonList(mock(Showcase.class));

        doReturn(expected)
                .when(mockService)
                .findAllShowcasesByUser(id);

        List<Showcase> actual = controller.findAllShowcasesByUser(id);

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForFindAllShowcasesByAlias() {
        String alias = "me";

        List<Showcase> expected = Collections.singletonList(
                mock(Showcase.class)
        );

        doReturn(expected)
                .when(mockService)
                .findAllShowcasesByAlias(alias);

        List<Showcase> actual = controller.findAllShowcasesByAlias(alias);

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForGetShowcaseById() {
        long id = 5;
        Showcase expected = mock(Showcase.class);

        doReturn(expected)
                .when(mockService)
                .getShowcaseById(id);

        Showcase actual = controller.getShowcaseById(id);

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForCreateShowcase() {
        Showcase submit = mock(Showcase.class);
        Showcase expected = mock(Showcase.class);

        doReturn(expected)
                .when(mockService)
                .createShowcase(submit);

        Showcase actual = controller.createShowcase(submit);

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForUpdateShowcase() {
        Showcase submit = mock(Showcase.class);
        Showcase expected = mock(Showcase.class);

        doReturn(expected)
                .when(mockService)
                .updateShowcase(submit);

        Showcase actual = controller.updateShowcase(submit);

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResultsForDeleteShowcase() {
        Showcase submit = mock(Showcase.class);
        VoidMethodResponse expected = mock(VoidMethodResponse.class);

        doReturn(expected)
                .when(mockService)
                .deleteShowcase(submit);

        VoidMethodResponse actual = controller.deleteShowcase(submit);

        assertSame(expected, actual);
    }

}

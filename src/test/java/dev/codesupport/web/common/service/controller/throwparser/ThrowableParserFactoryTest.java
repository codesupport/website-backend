package dev.codesupport.web.common.service.controller.throwparser;

import dev.codesupport.testutils.controller.throwparsing.parser.ThrowableParser;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ThrowableParserFactoryTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerIfThrowableIsNull() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        doReturn(Collections.emptyMap())
                .when(mockContext)
                .getBeansOfType(AbstractThrowableParser.class);

        ThrowableParserFactory factorySpy = Mockito.spy(new ThrowableParserFactory(mockContext));

        factorySpy.createParser(null);
    }

    @Test
    public void shouldCreateExpectedParserIfExistsInMap() {
        String throwableParserName = "throwableParser";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractThrowableParser mockThrowableParser = mock(AbstractThrowableParser.class);

        Throwable mockThrowable = mock(Throwable.class);

        Throwable mockRootThrowable = mock(Throwable.class);

        AbstractThrowableParser mockInstantiatedThrowableParser = mock(AbstractThrowableParser.class);
        ReflectionTestUtils.setField(mockInstantiatedThrowableParser, "throwable", mockRootThrowable);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        doReturn(mockInstantiatedThrowableParser)
                .when(mockThrowableParser)
                .instantiate(mockRootThrowable);

        doReturn(
                Collections.singletonMap(
                        throwableParserName,
                        mockThrowableParser
                )
        )
                .when(mockContext)
                .getBeansOfType(AbstractThrowableParser.class);

        ThrowableParserFactory factorySpy = Mockito.spy(new ThrowableParserFactory(mockContext));

        //ThrowableNotThrown - we are mocking, not throwing exceptions
        //noinspection ThrowableNotThrown
        doReturn(mockRootThrowable)
                .when(factorySpy)
                .getRootCause(mockThrowable);

        doReturn(throwableParserName)
                .when(factorySpy)
                .getParserNameFromException(mockRootThrowable);

        AbstractThrowableParser actual = factorySpy.createParser(mockThrowable);

        assertEquals(mockInstantiatedThrowableParser, actual);
    }

    @Test
    public void shouldCreateDefaultParserIfDoesntExistInMap() {
        String throwableParserName = "throwableParser";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractThrowableParser mockThrowableParser = mock(AbstractThrowableParser.class);

        Throwable mockThrowable = mock(Throwable.class);

        Throwable mockRootThrowable = mock(Throwable.class);

        AbstractThrowableParser mockInstantiatedThrowableParser = mock(AbstractThrowableParser.class);
        ReflectionTestUtils.setField(mockInstantiatedThrowableParser, "throwable", mockRootThrowable);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        doReturn(mockInstantiatedThrowableParser)
                .when(mockThrowableParser)
                .instantiate(mockRootThrowable);

        doReturn(Collections.emptyMap())
                .when(mockContext)
                .getBeansOfType(AbstractThrowableParser.class);

        ThrowableParserFactory factorySpy = Mockito.spy(new ThrowableParserFactory(mockContext));

        //ThrowableNotThrown - we are mocking, not throwing exceptions
        //noinspection ThrowableNotThrown
        doReturn(mockRootThrowable)
                .when(factorySpy)
                .getRootCause(mockThrowable);

        doReturn(throwableParserName)
                .when(factorySpy)
                .getParserNameFromException(mockRootThrowable);

        AbstractThrowableParser actual = factorySpy.createParser(mockThrowable);

        assertEquals(new DefaultExceptionParser(), actual);
    }

    @Test
    public void shouldGetCorrectRootCauseIfNotInMap() {
        String throwableParserName = "throwableParser";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        doReturn(Collections.emptyMap())
                .when(mockContext)
                .getBeansOfType(AbstractThrowableParser.class);

        ThrowableParserFactory factorySpy = Mockito.spy(new ThrowableParserFactory(mockContext));

        Throwable expected = new Throwable("Child throwable");
        Throwable throwable = new Throwable("Parent throwable", expected);

        doReturn(throwableParserName)
                .when(factorySpy)
                .getParserNameFromException(throwable);

        doReturn(throwableParserName)
                .when(factorySpy)
                .getParserNameFromException(expected);

        Throwable actual = factorySpy.getRootCause(throwable);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetCorrectRootCauseIfFoundInMap() {
        String throwableParserName = "throwableParser";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        doReturn(
                Collections.singletonMap(
                        throwableParserName,
                        new ThrowableParser()
                )
        )
                .when(mockContext)
                .getBeansOfType(AbstractThrowableParser.class);

        ThrowableParserFactory factorySpy = Mockito.spy(new ThrowableParserFactory(mockContext));

        Throwable throwable = new Throwable("Child throwable");
        Throwable expected = new Throwable("Parent throwable", throwable);

        doReturn(throwableParserName)
                .when(factorySpy)
                .getParserNameFromException(throwable);

        doReturn(throwableParserName)
                .when(factorySpy)
                .getParserNameFromException(expected);

        Throwable actual = factorySpy.getRootCause(expected);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetCorrectParserNameFromException() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        doReturn(Collections.emptyMap())
                .when(mockContext)
                .getBeansOfType(AbstractThrowableParser.class);

        ThrowableParserFactory factorySpy = Mockito.spy(new ThrowableParserFactory(mockContext));

        String expected = "throwableParser";

        String actual = factorySpy.getParserNameFromException(new Throwable());

        assertEquals(expected, actual);
    }
}

package dev.codesupport.web.common.service.controller.throwparser;

import dev.codesupport.testutils.controller.throwparsing.parser.ThrowableParser;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class AbstractThrowableParserTest {

    @Test
    public void shouldReturnParserFromInstantiateMethod() {
        AbstractThrowableParser<Throwable> parserSpy = Mockito.spy(new ThrowableParser());

        Throwable mockThrowable = mock(Throwable.class);

        //unchecked - this is fine for the purposes of this test.
        //noinspection unchecked
        AbstractThrowableParser<Throwable> mockInstantiatedThrowableParser = mock(AbstractThrowableParser.class);

        doReturn(mockInstantiatedThrowableParser)
                .when(parserSpy)
                .instantiate();

        AbstractThrowableParser<Throwable> actual = parserSpy.instantiate(mockThrowable);

        assertEquals(mockInstantiatedThrowableParser, actual);
    }

    @Test
    public void shouldHaveThrowableOnInstantiatedParser() {
        AbstractThrowableParser<Throwable> parserSpy = Mockito.spy(new ThrowableParser());

        Throwable mockThrowable = mock(Throwable.class);

        //unchecked - this is fine for the purposes of this test.
        //noinspection unchecked
        AbstractThrowableParser<Throwable> mockInstantiatedThrowableParser = mock(AbstractThrowableParser.class);

        doReturn(mockInstantiatedThrowableParser)
                .when(parserSpy)
                .instantiate();

        AbstractThrowableParser<Throwable> parser = parserSpy.instantiate(mockThrowable);

        Throwable actual = (Throwable) ReflectionTestUtils.getField(parser, "throwable");

        assertEquals(mockThrowable, actual);
    }

    @Test
    public void shouldModifyResponseMessageAndStatus() {
        String referenceId = "123";
        String parserMessage = "Parser message";

        RestResponse<Serializable> actual = new RestResponse<>();
        actual.setReferenceId(referenceId);

        AbstractThrowableParser<Throwable> parserSpy = Mockito.spy(new ThrowableParser());

        doReturn(parserMessage)
                .when(parserSpy)
                .responseMessage();

        doReturn(RestStatus.WARNING)
                .when(parserSpy)
                .responseStatus();

        RestResponse<Serializable> expected = new RestResponse<>();
        expected.setReferenceId(referenceId);
        expected.setMessage(parserMessage);
        expected.setStatus(RestStatus.WARNING);

        parserSpy.modifyResponse(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnFailedRestStatusByDefault() {
        AbstractThrowableParser<Throwable> parserSpy = Mockito.spy(new ThrowableParser());

        RestStatus expected = RestStatus.WARNING;
        RestStatus actual = parserSpy.responseStatus();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNoClassParameterSet() {
        //This is the point of this test.
        //noinspection rawtypes
        new AbstractThrowableParser() {
            @Override
            protected AbstractThrowableParser<Throwable> instantiate() {
                return null;
            }

            @Override
            protected String responseMessage() {
                return null;
            }
        };
    }

    @Test
    public void shouldSetCorrectThrowableClassType() {
        AbstractThrowableParser<Throwable> parserSpy = Mockito.spy(new ThrowableParser());

        Class<Throwable> expected = Throwable.class;
        //This is fine for the purposes of this test.
        //noinspection unchecked,rawtypes
        Class<Throwable> actual = (Class) ReflectionTestUtils.getField(parserSpy, "throwableClassType");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectThrowableClassType() {
        AbstractThrowableParser<Throwable> parserSpy = Mockito.spy(new ThrowableParser());

        String expected = Throwable.class.getCanonicalName();
        String actual = parserSpy.getThrowableClassType();

        assertEquals(expected, actual);
    }

}

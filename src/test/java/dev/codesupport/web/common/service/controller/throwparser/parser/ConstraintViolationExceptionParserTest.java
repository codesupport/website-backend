package dev.codesupport.web.common.service.controller.throwparser.parser;

import com.google.common.collect.Sets;
import dev.codesupport.testutils.collection.MockNode;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ConstraintViolationExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        ConstraintViolationExceptionParser parser = new ConstraintViolationExceptionParser();

        AbstractThrowableParser<ConstraintViolationException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof ConstraintViolationExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        ConstraintViolationExceptionParser parser = new ConstraintViolationExceptionParser();

        AbstractThrowableParser<ConstraintViolationException> firstInstance = parser.instantiate();
        AbstractThrowableParser<ConstraintViolationException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        ConstraintViolationException mockException = mock(ConstraintViolationException.class);

        //rawtypes - This is fine for the purposes of this test.
        //noinspection rawtypes
        ConstraintViolation mockConstraintViolation = mock(ConstraintViolation.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Set<ConstraintViolation<?>> mockConstraintSet = Sets.newHashSet(mockConstraintViolation);

        doReturn((Path) () ->
                IteratorUtils.arrayIterator(
                        new Path.Node[]{new MockNode()}
                )
        )
                .when(mockConstraintViolation)
                .getPropertyPath();

        doReturn("apple")
                .when(mockConstraintViolation)
                .getInvalidValue();

        doReturn("test message")
                .when(mockConstraintViolation)
                .getMessage();

        doReturn(mockConstraintSet)
                .when(mockException)
                .getConstraintViolations();

        ConstraintViolationExceptionParser parser = new ConstraintViolationExceptionParser();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        String expected = "Invalid parameter(s): [MockNode 'apple': test message]";

        assertEquals(expected, parser.responseMessage());
    }

    @Test
    public void shouldReturnCorrectStatus() {
        ServiceLayerExceptionParser parser = new ServiceLayerExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}

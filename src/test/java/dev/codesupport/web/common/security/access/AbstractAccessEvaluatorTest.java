package dev.codesupport.web.common.security.access;

import dev.codesupport.web.common.exception.InvalidArgumentException;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class AbstractAccessEvaluatorTest {

    private static final String CLASS_TYPE = "classType";

    private static class TestAccessEvaluator extends AbstractAccessEvaluator<Long> {

        public TestAccessEvaluator() {
            super(Permission.READ);
        }

        @Override
        protected boolean hasPermissionCheck(Authentication auth, Long targetDomainObject) {
            return false;
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfParameterClassNotDefined() {
        //rawtypes - This is what we are testing.
        //noinspection rawtypes
        new AbstractAccessEvaluator(Permission.READ) {
            @Override
            protected boolean hasPermissionCheck(Authentication auth, Object targetDomainObject) {
                return false;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfParameterIsList() {
        new AbstractAccessEvaluator<List<Long>>(Permission.READ) {
            @Override
            protected boolean hasPermissionCheck(Authentication auth, List<Long> targetDomainObject) {
                return false;
            }
        };
    }

    @Test
    public void shouldCorrectlyStoreClassType() {
        TestAccessEvaluator evaluator = new TestAccessEvaluator();

        Class<?> classType = (Class<?>) ReflectionTestUtils.getField(
                evaluator,
                CLASS_TYPE
        );

        assertEquals(Long.class, classType);
    }

    @Test
    public void shouldReturnCorrectEvaluatorNameWithNoAccessor() {
        TestAccessEvaluator evaluator = new TestAccessEvaluator();

        ReflectionTestUtils.setField(
                evaluator,
                CLASS_TYPE,
                Long.class
        );

        String expected = Permission.READ.toString().toLowerCase() + " " + Long.class.getCanonicalName();
        String actual = evaluator.getEvaluatorName();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectEvaluatorNameWithAccessor() {
        TestAccessEvaluator evaluatorSpy = spy(new TestAccessEvaluator());

        doReturn(Accessor.DISCORD)
                .when(evaluatorSpy)
                .getAccessor();

        ReflectionTestUtils.setField(
                evaluatorSpy,
                CLASS_TYPE,
                Long.class
        );

        String expected = Permission.READ.toString().toLowerCase() + " " + Accessor.DISCORD.toString().toLowerCase();
        String actual = evaluatorSpy.getEvaluatorName();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectDefaultAccessorValue() {
        TestAccessEvaluator evaluator = new TestAccessEvaluator();

        Accessor actual = evaluator.getAccessor();

        assertEquals(Accessor.NONE, actual);
    }

    @Test(expected = InvalidArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionIfIncorrectObject() {
        Authentication mockAuthentication = mock(Authentication.class);
        Object object = "string";

        TestAccessEvaluator evaluatorSpy = spy(new TestAccessEvaluator());

        Class<Long> longClass = Long.class;

        ReflectionTestUtils.setField(
                evaluatorSpy,
                CLASS_TYPE,
                longClass
        );

        evaluatorSpy.hasPermission(mockAuthentication, object);
    }

    @Test(expected = InvalidArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionIfListWithInvalidObject() {
        Long longVal = 5L;

        Authentication mockAuthentication = mock(Authentication.class);
        List<Object> objects = new ArrayList<>();
        objects.add(longVal);
        objects.add("String");

        TestAccessEvaluator evaluatorSpy = spy(new TestAccessEvaluator());

        doReturn(true)
                .when(evaluatorSpy)
                .hasPermissionCheck(mockAuthentication, longVal);

        Class<Long> longClass = Long.class;

        ReflectionTestUtils.setField(
                evaluatorSpy,
                CLASS_TYPE,
                longClass
        );

        evaluatorSpy.hasPermission(mockAuthentication, objects);
    }

    @Test
    public void shouldReturnCorrectPermissionIfCorrectObjectList() {
        Long longVal = 5L;

        Authentication mockAuthentication = mock(Authentication.class);
        List<Object> objects = new ArrayList<>();
        objects.add(longVal);
        objects.add(longVal);

        TestAccessEvaluator evaluatorSpy = spy(new TestAccessEvaluator());

        doReturn(true)
                .when(evaluatorSpy)
                .hasPermissionCheck(mockAuthentication, longVal);

        Class<Long> longClass = Long.class;

        ReflectionTestUtils.setField(
                evaluatorSpy,
                CLASS_TYPE,
                longClass
        );

        assertTrue(
                evaluatorSpy.hasPermission(mockAuthentication, objects)
        );
    }

    @Test
    public void shouldReturnCorrectPermissionIfCorrectObject() {
        Authentication mockAuthentication = mock(Authentication.class);
        Long object = 5L;

        TestAccessEvaluator evaluatorSpy = spy(new TestAccessEvaluator());

        Class<Long> longClass = Long.class;

        ReflectionTestUtils.setField(
                evaluatorSpy,
                CLASS_TYPE,
                longClass
        );

        doReturn(true)
                .when(evaluatorSpy)
                .hasPermissionCheck(mockAuthentication, object);

        assertTrue(
                evaluatorSpy.hasPermission(mockAuthentication, object)
        );
    }

    @Test
    public void shouldReturnFalseIfAuthenticationNull() {
        TestAccessEvaluator evaluator = new TestAccessEvaluator();

        assertFalse(evaluator.isValidAuth(null));
    }

    @Test
    public void shouldReturnFalseIfAuthenticationAnonymous() {
        TestAccessEvaluator evaluator = new TestAccessEvaluator();

        Authentication mockAuthentication = mock(AnonymousAuthenticationToken.class);

        assertFalse(evaluator.isValidAuth(mockAuthentication));
    }

    @Test
    public void shouldReturnTrueIfValidAuthentication() {
        TestAccessEvaluator evaluator = new TestAccessEvaluator();

        Authentication mockAuthentication = mock(UsernamePasswordAuthenticationToken.class);

        assertTrue(evaluator.isValidAuth(mockAuthentication));
    }

}

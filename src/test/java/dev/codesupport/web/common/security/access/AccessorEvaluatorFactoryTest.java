package dev.codesupport.web.common.security.access;

import dev.codesupport.web.common.exception.InvalidArgumentException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class AccessorEvaluatorFactoryTest {

    private static final String STRING_EVALUATOR_MAP = "stringEvaluatorMap";
    private static final String CLASS_EVALUATOR_MAP = "evaluatorMap";

    @Test
    public void shouldPopulateStringEvaluatorMap() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockStringEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(String.class.getCanonicalName())
                .when(mockStringEvaluator)
                .getEvaluatorClassType();

        doReturn(Accessor.NONE)
                .when(mockStringEvaluator)
                .getAccessor();

        AbstractAccessEvaluator<?> mockEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(Object.class.getCanonicalName())
                .when(mockEvaluator)
                .getEvaluatorClassType();

        //rawtypes - This is fine for the purposes of this test
        //noinspection rawtypes
        Map<String, AbstractAccessEvaluator> evaluatorMap = new HashMap<>();
        evaluatorMap.put("SomeAbstractAccessEvaluator", mockStringEvaluator);
        evaluatorMap.put("SomeOtherEvaluator", mockEvaluator);

        doReturn(evaluatorMap)
                .when(mockContext)
                .getBeansOfType(AbstractAccessEvaluator.class);

        AccessEvaluatorFactory accessEvaluatorFactory = new AccessEvaluatorFactory(mockContext);

        //rawtypes - This is fine for the purposes of this test
        //noinspection rawtypes
        Map<String, AbstractAccessEvaluator> expected = Collections.singletonMap(
                "none",
                mockStringEvaluator
        );

        //unchecked - This is fine for the purposes of this test
        //rawtypes - This is fine for the purposes of this test
        //noinspection unchecked,rawtypes
        Map<String, AbstractAccessEvaluator> actual = (Map<String, AbstractAccessEvaluator>) ReflectionTestUtils.getField(
                accessEvaluatorFactory,
                STRING_EVALUATOR_MAP
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldPopulateClassEvaluatorMap() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockStringEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(String.class.getCanonicalName())
                .when(mockStringEvaluator)
                .getEvaluatorClassType();

        doReturn(Accessor.NONE)
                .when(mockStringEvaluator)
                .getAccessor();

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(Object.class.getCanonicalName())
                .when(mockClassEvaluator)
                .getEvaluatorClassType();

        //rawtypes - This is fine for the purposes of this test
        //noinspection rawtypes
        Map<String, AbstractAccessEvaluator> evaluatorMap = new HashMap<>();
        evaluatorMap.put("SomeAbstractAccessEvaluator", mockStringEvaluator);
        evaluatorMap.put("SomeOtherEvaluator", mockClassEvaluator);

        doReturn(evaluatorMap)
                .when(mockContext)
                .getBeansOfType(AbstractAccessEvaluator.class);

        AccessEvaluatorFactory accessEvaluatorFactory = new AccessEvaluatorFactory(mockContext);

        //rawtypes - This is fine for the purposes of this test
        //noinspection rawtypes
        Map<String, AbstractAccessEvaluator> expected = Collections.singletonMap(
                Object.class.getCanonicalName(),
                mockClassEvaluator
        );

        //unchecked - This is fine for the purposes of this test
        //rawtypes - This is fine for the purposes of this test
        //noinspection unchecked,rawtypes
        Map<String, AbstractAccessEvaluator> actual = (Map<String, AbstractAccessEvaluator>) ReflectionTestUtils.getField(
                accessEvaluatorFactory,
                CLASS_EVALUATOR_MAP
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetCorrectEvaluatorByObjectClassType() {
        Long object = 5L;

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockClassEvaluatorMap = mock(HashMap.class);
        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockStringEvaluatorMap = mock(HashMap.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(false)
                .when(mockStringEvaluatorMap)
                .containsKey(anyString());

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                CLASS_EVALUATOR_MAP,
                mockClassEvaluatorMap
        );

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                STRING_EVALUATOR_MAP,
                mockStringEvaluatorMap
        );

        doReturn(mockClassEvaluator)
                .when(accessEvaluatorFactorySpy)
                .getEvaluatorByName(object.getClass().getCanonicalName());

        AbstractAccessEvaluator<?> actual = accessEvaluatorFactorySpy.getEvaluator(object);

        assertEquals(mockClassEvaluator, actual);
    }

    @Test(expected = InvalidArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionIfEvaluatorDoesntExist() {
        String object = "string";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockClassEvaluatorMap = mock(HashMap.class);
        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockStringEvaluatorMap = mock(HashMap.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(false)
                .when(mockStringEvaluatorMap)
                .containsKey(object);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                CLASS_EVALUATOR_MAP,
                mockClassEvaluatorMap
        );

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                STRING_EVALUATOR_MAP,
                mockStringEvaluatorMap
        );

        accessEvaluatorFactorySpy.getEvaluator(object);
    }

    @Test
    public void shouldGetCorrectEvaluatorByObjectClassTypeIfString() {
        String object = "string";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockStringEvaluator = mock(AbstractAccessEvaluator.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockClassEvaluatorMap = mock(HashMap.class);
        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockStringEvaluatorMap = mock(HashMap.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(true)
                .when(mockStringEvaluatorMap)
                .containsKey(object);

        doReturn(mockStringEvaluator)
                .when(mockStringEvaluatorMap)
                .get(object);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                CLASS_EVALUATOR_MAP,
                mockClassEvaluatorMap
        );

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                STRING_EVALUATOR_MAP,
                mockStringEvaluatorMap
        );

        AbstractAccessEvaluator<?> actual = accessEvaluatorFactorySpy.getEvaluator(object);

        assertEquals(mockStringEvaluator, actual);
    }

    @Test(expected = InvalidArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionIfCanonicalClassNameNotInMap() {
        String object = "java.lang.String";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockClassEvaluatorMap = mock(HashMap.class);
        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockStringEvaluatorMap = mock(HashMap.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(false)
                .when(mockClassEvaluatorMap)
                .containsKey(object);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                CLASS_EVALUATOR_MAP,
                mockClassEvaluatorMap
        );

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                STRING_EVALUATOR_MAP,
                mockStringEvaluatorMap
        );

        accessEvaluatorFactorySpy.getEvaluatorByName(object);
    }

    @Test
    public void shouldGetCorrectEvaluatorByCanonicalClassName() {
        String object = "java.lang.String";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockClassEvaluatorMap = mock(HashMap.class);
        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockStringEvaluatorMap = mock(HashMap.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(true)
                .when(mockClassEvaluatorMap)
                .containsKey(object);

        doReturn(mockClassEvaluator)
                .when(mockClassEvaluatorMap)
                .get(object);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                CLASS_EVALUATOR_MAP,
                mockClassEvaluatorMap
        );

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                STRING_EVALUATOR_MAP,
                mockStringEvaluatorMap
        );

        AbstractAccessEvaluator<?> actual = accessEvaluatorFactorySpy.getEvaluatorByName(object);

        assertEquals(mockClassEvaluator, actual);
    }
}

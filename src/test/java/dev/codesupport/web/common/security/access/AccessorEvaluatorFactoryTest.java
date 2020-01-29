package dev.codesupport.web.common.security.access;

import dev.codesupport.web.common.exception.InvalidArgumentException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class AccessorEvaluatorFactoryTest {

    private static final String EVALUATOR_MAP = "evaluatorMap";

    @Test
    public void shouldPopulateEvaluatorMap() {
        String evaluatorName1 = "evaluator 1";
        String evaluatorName2 = "evaluator 2";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockStringEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(evaluatorName1)
                .when(mockStringEvaluator)
                .getEvaluatorName();

        doReturn(Accessor.NONE)
                .when(mockStringEvaluator)
                .getAccessor();

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(evaluatorName2)
                .when(mockClassEvaluator)
                .getEvaluatorName();

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
        Map<String, AbstractAccessEvaluator> expected = new HashMap<>();
        expected.put(
                evaluatorName1,
                mockStringEvaluator
        );
        expected.put(
                evaluatorName2,
                mockClassEvaluator
        );

        //unchecked - This is fine for the purposes of this test
        //rawtypes - This is fine for the purposes of this test
        //noinspection unchecked,rawtypes
        Map<String, AbstractAccessEvaluator> actual = (Map<String, AbstractAccessEvaluator>) ReflectionTestUtils.getField(
                accessEvaluatorFactory,
                EVALUATOR_MAP
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetCorrectEvaluatorForStringObject() {
        String object = "String";
        String permission = "read";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        doReturn(mockClassEvaluator)
                .when(accessEvaluatorFactorySpy)
                .getEvaluatorByName(
                        object,
                        permission
                );

        AbstractAccessEvaluator<?> actual = accessEvaluatorFactorySpy.getEvaluator(object, permission);

        assertEquals(mockClassEvaluator, actual);
    }

    @Test(expected = InvalidArgumentException.class)
    public void shouldBubbleInvalidArgumentExceptionForNullObject() {
        String permission = "read";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        doThrow(InvalidArgumentException.class)
                .when(accessEvaluatorFactorySpy)
                .getEvaluatorByName(
                        null,
                        permission
                );

        AbstractAccessEvaluator<?> actual = accessEvaluatorFactorySpy.getEvaluator(null, permission);

        assertEquals(mockClassEvaluator, actual);
    }

    @Test
    public void shouldGetCorrectEvaluatorForListObject() {
        List<Long> objects = Collections.singletonList(5L);
        String permission = "read";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        doReturn(mockClassEvaluator)
                .when(accessEvaluatorFactorySpy)
                .getEvaluatorByName(
                        objects.get(0).getClass().getCanonicalName(),
                        permission
                );

        AbstractAccessEvaluator<?> actual = accessEvaluatorFactorySpy.getEvaluator(objects, permission);

        assertEquals(mockClassEvaluator, actual);
    }

    @Test
    public void shouldGetCorrectEvaluatorForNonStringObject() {
        Long object = 5L;
        String permission = "read";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        doReturn(mockClassEvaluator)
                .when(accessEvaluatorFactorySpy)
                .getEvaluatorByName(
                        object.getClass().getCanonicalName(),
                        permission
                );

        AbstractAccessEvaluator<?> actual = accessEvaluatorFactorySpy.getEvaluator(object, permission);

        assertEquals(mockClassEvaluator, actual);
    }

    @Test(expected = InvalidArgumentException.class)
    public void shouldThrowInvalidArgumentExceptionIfEvaluatorNameNotInMap() {
        String object = String.class.getCanonicalName();
        String permission = "read";

        String evaluatorName = permission + " " + object;

        ApplicationContext mockContext = mock(ApplicationContext.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockEvaluatorMap = mock(HashMap.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(false)
                .when(mockEvaluatorMap)
                .containsKey(evaluatorName);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                EVALUATOR_MAP,
                mockEvaluatorMap
        );

        accessEvaluatorFactorySpy.getEvaluatorByName(
                object,
                permission
        );
    }

    @Test
    public void shouldGetCorrectEvaluatorByName() {
        String object = String.class.getCanonicalName();
        String permission = "read";

        String evaluatorName = permission + " " + object;

        ApplicationContext mockContext = mock(ApplicationContext.class);

        AbstractAccessEvaluator<?> mockClassEvaluator = mock(AbstractAccessEvaluator.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, AbstractAccessEvaluator<?>> mockEvaluatorMap = mock(HashMap.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(true)
                .when(mockEvaluatorMap)
                .containsKey(evaluatorName);

        doReturn(mockClassEvaluator)
                .when(mockEvaluatorMap)
                .get(evaluatorName);

        AccessEvaluatorFactory accessEvaluatorFactorySpy = spy(new AccessEvaluatorFactory(mockContext));

        ReflectionTestUtils.setField(
                accessEvaluatorFactorySpy,
                EVALUATOR_MAP,
                mockEvaluatorMap
        );

        AbstractAccessEvaluator<?> actual = accessEvaluatorFactorySpy
                .getEvaluatorByName(
                        object,
                        permission
                );

        assertEquals(mockClassEvaluator, actual);
    }
}

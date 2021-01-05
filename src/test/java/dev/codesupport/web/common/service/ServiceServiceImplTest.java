package dev.codesupport.web.common.service;

import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.domain.Endpoint;
import dev.codesupport.web.domain.UrlParameter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//S5976 - ParameterizedTests - Not doing that right now.
@SuppressWarnings({"java:S5976"})
public class ServiceServiceImplTest {

    private ServiceServiceImpl serviceSpy;
    private static ApplicationContext mockContext;

    @BeforeClass
    public static void init() {
        mockContext = mock(ApplicationContext.class);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockContext
        );
        serviceSpy = spy(new ServiceServiceImpl(mockContext));
    }

    @Test
    public void shouldCreateEmptySet() {
        doReturn(Collections.emptyMap())
                .when(mockContext)
                .getBeansWithAnnotation(RestController.class);

        serviceSpy.init();

        Set<Endpoint> expected = Collections.emptySet();
        Object actual = ReflectionTestUtils.getField(serviceSpy, "endpoints");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldInvokeAnalyzeMethodsForEveryBean() {
        String pathA = "/pathA";
        String pathB = "/pathB";

        Map<String, Object> contextBeans = new HashMap<>();
        contextBeans.put("controllerA", new ControllerA());
        contextBeans.put("controllerB", new ControllerB());

        doReturn(contextBeans)
                .when(mockContext)
                .getBeansWithAnnotation(RestController.class);

        doReturn(InterfaceA.class)
                .when(serviceSpy)
                .getControllerInterface(ControllerA.class);

        doReturn(InterfaceB.class)
                .when(serviceSpy)
                .getControllerInterface(ControllerB.class);

        doReturn(pathA)
                .when(serviceSpy)
                .getControllerPath(InterfaceA.class);

        doReturn(pathB)
                .when(serviceSpy)
                .getControllerPath(InterfaceB.class);

        doNothing()
                .when(serviceSpy)
                .analyzeMethods(any(Method[].class), anyString());

        Method[] interfaceAMethods = InterfaceA.class.getDeclaredMethods();
        Method[] interfaceBMethods = InterfaceB.class.getDeclaredMethods();

        serviceSpy.init();

        verify(serviceSpy, times(1))
                .analyzeMethods(interfaceAMethods, pathA);
        verify(serviceSpy, times(1))
                .analyzeMethods(interfaceBMethods, pathB);
    }

    @Test
    public void shouldGetControllerInterface() {
        Class<?> expected = InterfaceB.class;
        Class<?> actual = serviceSpy.getControllerInterface(ControllerBExtended.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldAnalyzeMethods() {
        String path = "/path";
        int version = 1;

        doReturn(version)
                .when(serviceSpy)
                .getControllerPathVersion(path);

        doNothing()
                .when(serviceSpy)
                .analyzeMethod(any(Method.class), anyString(), anyInt());

        Method[] methods = InterfaceA.class.getMethods();

        serviceSpy.analyzeMethods(methods, path);

        // Verify the method was only run once for every method
        verify(serviceSpy, times(methods.length))
                .analyzeMethod(any(Method.class), anyString(), anyInt());

        // Verify each method was run
        for (Method method : methods) {
            verify(serviceSpy, times(1))
                    .analyzeMethod(method, path, version);
        }
    }

    @Test
    public void shouldAddAnalyzedMethod() throws NoSuchMethodException {
        ReflectionTestUtils.setField(serviceSpy, "endpoints", new HashSet<>());

        int version = 1;
        String interfacePath = "/path";
        String methodPath = "/there";
        String requestMethod = "GET";
        String methodName = "getMethod";
        String returnType = "String";
        Set<UrlParameter> urlParameters = new HashSet<>();
        urlParameters.add(new UrlParameter());

        Method interfaceMethod = InterfaceA.class.getMethod(methodName, String.class, int.class, boolean.class);

        doReturn(requestMethod)
                .when(serviceSpy)
                .getRequestMethod(interfaceMethod);

        doReturn(methodName)
                .when(serviceSpy)
                .getRequestMethodName(interfaceMethod);

        doReturn(methodPath)
                .when(serviceSpy)
                .getRequestPath(interfaceMethod, requestMethod);

        doReturn(returnType)
                .when(serviceSpy)
                .getRequestReturnType(interfaceMethod);

        doReturn(true)
                .when(serviceSpy)
                .containsRequestBody(interfaceMethod);

        doReturn(urlParameters)
                .when(serviceSpy)
                .getUrlParameters(interfaceMethod);

        serviceSpy.analyzeMethod(interfaceMethod, interfacePath, version);

        Set<Endpoint> expected = Collections.singleton(
                new Endpoint(
                        methodName,
                        version,
                        requestMethod,
                        interfacePath + methodPath,
                        returnType,
                        true,
                        urlParameters
                )
        );
        Object actual = ReflectionTestUtils.getField(serviceSpy, "endpoints");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotAddAnalyzedMethod() throws NoSuchMethodException {
        ReflectionTestUtils.setField(serviceSpy, "endpoints", new HashSet<>());

        int version = 1;
        String interfacePath = "/path";
        String methodName = "getMethod";

        Method interfaceMethod = InterfaceA.class.getMethod(methodName, String.class, int.class, boolean.class);

        doReturn("")
                .when(serviceSpy)
                .getRequestMethod(interfaceMethod);

        doReturn(null)
                .when(serviceSpy)
                .getRequestMethodName(any(Method.class));

        doReturn(null)
                .when(serviceSpy)
                .getRequestPath(any(Method.class), anyString());

        doReturn(null)
                .when(serviceSpy)
                .getRequestReturnType(any(Method.class));

        doReturn(false)
                .when(serviceSpy)
                .containsRequestBody(any(Method.class));

        doReturn(null)
                .when(serviceSpy)
                .getUrlParameters(any(Method.class));

        serviceSpy.analyzeMethod(interfaceMethod, interfacePath, version);

        Set<Endpoint> expected = Collections.emptySet();
        Object actual = ReflectionTestUtils.getField(serviceSpy, "endpoints");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRequestMethod() throws NoSuchMethodException {
        String expected = "DELETE";
        String methodName = "deleteMethod";

        Method interfaceMethod = InterfaceA.class.getMethod(methodName, String.class);
        Annotation methodAnnotation = interfaceMethod.getAnnotation(DeleteMapping.class);

        Optional<String> requestMethodOptional = Optional.of(expected);

        doReturn(Optional.empty())
                .when(serviceSpy)
                .getRequestMappingMethod(any(Annotation.class));

        doReturn(Optional.empty())
                .when(serviceSpy)
                .getRequestMappingMethod(null);

        doReturn(requestMethodOptional)
                .when(serviceSpy)
                .getRequestMappingMethod(methodAnnotation);

        String actual = serviceSpy.getRequestMethod(interfaceMethod);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmtpyStringForRequestMethod() throws NoSuchMethodException {
        String methodName = "otherMethod";

        Method interfaceMethod = InterfaceA.class.getMethod(methodName, String.class);

        doReturn(Optional.empty())
                .when(serviceSpy)
                .getRequestMappingMethod(null);

        String expected = "";
        String actual = serviceSpy.getRequestMethod(interfaceMethod);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetRequestMethodName() throws NoSuchMethodException {
        ReflectionTestUtils.setField(serviceSpy, "endpoints", new HashSet<>());

        String expected = "otherMethod";

        Method interfaceMethod = InterfaceA.class.getMethod(expected, String.class);

        String actual = serviceSpy.getRequestMethodName(interfaceMethod);

        assertEquals(expected, actual);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionIfDuplicateRequestMethodName() throws NoSuchMethodException {
        String expected = "otherMethod";

        ReflectionTestUtils.setField(
                serviceSpy,
                "endpoints",
                Collections.singleton(
                        new Endpoint(expected, 0, null, null, null, false, null)
                )
        );

        Method interfaceMethod = InterfaceA.class.getMethod(expected, String.class);

        serviceSpy.getRequestMethodName(interfaceMethod);
    }

    @Test
    public void shouldReturnFoundRequestMappingMethod() throws NoSuchMethodException {
        Method interfaceMethod = InterfaceA.class.getMethod("getMethod", String.class, int.class, boolean.class);
        Annotation mappingAnnotation = interfaceMethod.getAnnotation(GetMapping.class);

        Optional<String> expected = Optional.of("GET");
        Optional<String> actual = serviceSpy.getRequestMappingMethod(mappingAnnotation);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyOptionalForNullAnnotationInRequestMappingMethod() {
        Optional<String> expected = Optional.empty();
        Optional<String> actual = serviceSpy.getRequestMappingMethod(null);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnPathForGetRequestPath() throws NoSuchMethodException {
        String requestMethod = "GET";

        Method interfaceMethod = InterfaceA.class.getMethod("getMethod", String.class, int.class, boolean.class);

        String expected = "/path";

        doReturn(null)
                .when(serviceSpy)
                .getMappingValues(any(), any());

        doReturn(new String[] { expected } )
                .when(serviceSpy)
                .getMappingValues(interfaceMethod, requestMethod);

        String actual = serviceSpy.getRequestPath(interfaceMethod, requestMethod);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyStringForGetRequestPath() throws NoSuchMethodException {
        String requestMethod = "GET";

        Method interfaceMethod = InterfaceA.class.getMethod("getMethod", String.class, int.class, boolean.class);

        doReturn(null)
                .when(serviceSpy)
                .getMappingValues(any(), any());

        doReturn(new String[] {} )
                .when(serviceSpy)
                .getMappingValues(interfaceMethod, requestMethod);

        String expected = "";
        String actual = serviceSpy.getRequestPath(interfaceMethod, requestMethod);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnPathValuesForGetMapping() throws NoSuchMethodException {
        String requestMethod = "GET";

        Method interfaceMethod = InterfaceA.class.getMethod("getMethod", String.class, int.class, boolean.class);

        String[] expected = new String[] { "/get" };
        String[] actual = serviceSpy.getMappingValues(interfaceMethod, requestMethod);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnPathValuesForPostMapping() throws NoSuchMethodException {
        String requestMethod = "POST";

        Method interfaceMethod = InterfaceA.class.getMethod("postMethod", String.class);

        String[] expected = new String[] { "/post" };
        String[] actual = serviceSpy.getMappingValues(interfaceMethod, requestMethod);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnPathValuesForPutMapping() throws NoSuchMethodException {
        String requestMethod = "PUT";

        Method interfaceMethod = InterfaceA.class.getMethod("putMethod", String.class);

        String[] expected = new String[] { "/put" };
        String[] actual = serviceSpy.getMappingValues(interfaceMethod, requestMethod);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnPathValuesForDeleteMapping() throws NoSuchMethodException {
        String requestMethod = "DELETE";

        Method interfaceMethod = InterfaceA.class.getMethod("deleteMethod", String.class);

        String[] expected = new String[] { "/delete" };
        String[] actual = serviceSpy.getMappingValues(interfaceMethod, requestMethod);

        assertArrayEquals(expected, actual);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionForInvalidRequestMethod() throws NoSuchMethodException {
        String requestMethod = "banana";

        Method interfaceMethod = InterfaceA.class.getMethod("deleteMethod", String.class);

        serviceSpy.getMappingValues(interfaceMethod, requestMethod);
    }

    @Test
    public void shouldGetRequestReturnType() throws NoSuchMethodException {
        Method interfaceMethod = InterfaceA.class.getMethod("getMethod", String.class, int.class, boolean.class);

        String expected = "String";
        String actual = serviceSpy.getRequestReturnType(interfaceMethod);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnTrueIfContainsRequestBody() throws NoSuchMethodException {
        Method interfaceMethod = InterfaceA.class.getMethod("postMethod", String.class);

        assertTrue(serviceSpy.containsRequestBody(interfaceMethod));
    }

    @Test
    public void shouldReturnFalseIfContainsNoRequestBody() throws NoSuchMethodException {
        Method interfaceMethod = InterfaceA.class.getMethod("getMethod", String.class, int.class, boolean.class);

        assertFalse(serviceSpy.containsRequestBody(interfaceMethod));
    }

    @Test
    public void shouldReturnControllerPath() {
        String expected = "/api";
        String actual = serviceSpy.getControllerPath(InterfaceA.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyStringForControllerPath() {
        String expected = "";
        String actual = serviceSpy.getControllerPath(InterfaceB.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnControllerPathVersion() {
        int expected = 1;
        String path = "/api/v" + expected;

        int actual = serviceSpy.getControllerPathVersion(path);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnZeroForUnversionedControllerPath() {
        String path = "/api/ve1";

        int expected = 0;
        int actual = serviceSpy.getControllerPathVersion(path);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldAddUrlParameters() throws NoSuchMethodException {
        Method interfaceMethod = InterfaceA.class.getMethod("getMethod", String.class, int.class, boolean.class);

        Parameter[] parameters = interfaceMethod.getParameters();

        doNothing()
                .when(serviceSpy)
                .addUrlParameter(any(Parameter.class), eq(Collections.emptySet()));

        serviceSpy.getUrlParameters(interfaceMethod);

        // Verify the method was only run once for every parameter
        verify(serviceSpy, times(parameters.length))
                .addUrlParameter(any(Parameter.class), eq(Collections.emptySet()));

        // Verify each parameter was run
        for (Parameter parameter : parameters) {
            verify(serviceSpy, times(1))
                    .addUrlParameter(parameter, Collections.emptySet());
        }
    }

    @Test
    public void shouldAddUrlParameter() throws NoSuchMethodException {
        Set<UrlParameter> actual = new HashSet<>();
        Parameter parameter = InterfaceA.class
                .getMethod("getMethod", String.class, int.class, boolean.class)
                .getParameters()[2];

        Set<UrlParameter> expected = Collections.singleton(
                new UrlParameter(
                        "parameterC",
                        "boolean",
                        true
                )
        );

        serviceSpy.addUrlParameter(parameter, actual);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEndpoints() {
        Set<Endpoint> expected = new HashSet<>();
        ReflectionTestUtils.setField(serviceSpy, "endpoints", expected);

        Set<Endpoint> actual = serviceSpy.getEndpoints();

        assertSame(expected, actual);
    }

    private static class ControllerBExtended extends ControllerB {

    }

    private static class ControllerA {

    }

    private static class ControllerB implements SomeOtherInterface, InterfaceB {

        @Override
        public String someMethod(String propertyA) {
            return null;
        }

    }

    private interface SomeOtherInterface {

    }

    //unused - This is a test class
    @SuppressWarnings("unused")
    @RestController
    @RequestMapping("/api")
    private interface InterfaceA {

        @GetMapping("/get")
        String getMethod(
                @RequestParam String parameterA,
                @RequestParam("paramB") int parameterB,
                @RequestParam(required = false) boolean parameterC
        );
        Boolean otherMethod(String notAUrlParameter);
        @PostMapping("/post")
        Date postMethod(@RequestBody String payload);
        @PutMapping("/put")
        Long putMethod(@RequestBody String payloadB);
        @DeleteMapping("/delete")
        Long deleteMethod(@RequestBody String payloadC);

    }

    //unused - This is a test class
    @SuppressWarnings("unused")
    @RestController
    private interface InterfaceB {

        @GetMapping
        String someMethod(@RequestParam String propertyA);

    }

}

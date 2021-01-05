package dev.codesupport.web.common.service;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.util.NumberUtils;
import dev.codesupport.web.domain.Endpoint;
import dev.codesupport.web.domain.UrlParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ApplicationContext context;
    private Set<Endpoint> endpoints;

    @PostConstruct
    public void init() {
        endpoints = new HashSet<>();
        Map<String, Object> beanMap = context.getBeansWithAnnotation(RestController.class);
        for (Object bean : beanMap.values()) {
            Class<?> controllerInterface = getControllerInterface(bean.getClass());

            if (controllerInterface != null) {
                String path = getControllerPath(controllerInterface);
                Method[] methods = controllerInterface.getDeclaredMethods();
                analyzeMethods(methods, path);
            }
        }
    }

    @VisibleForTesting
    Class<?> getControllerInterface(Class<?> controller) {
        Class<?> controllerInterface = null;
        Iterator<Class<?>> interfaces = Arrays.asList(controller.getSuperclass().getInterfaces()).iterator();

        while (controllerInterface == null && interfaces.hasNext()) {
            Class<?> interfaceClass = interfaces.next();
            RestController restController = interfaceClass.getAnnotation(RestController.class);
            if (restController != null) {
                controllerInterface = interfaceClass;
            }
        }

        return controllerInterface;
    }

    @VisibleForTesting
    void analyzeMethods(Method[] methods, String path) {
        int version = getControllerPathVersion(path);
        for (Method method : methods) {
            analyzeMethod(method, path, version);
        }
    }

    @VisibleForTesting
    void analyzeMethod(Method method, String path, int version) {
        String requestMethod = getRequestMethod(method);
        if (!requestMethod.isEmpty()) {
            String name = getRequestMethodName(method);
            String fullPath = path + getRequestPath(method, requestMethod);
            String returnType = getRequestReturnType(method);
            boolean expectsBody = containsRequestBody(method);
            Set<UrlParameter> urlParameters = getUrlParameters(method);

            endpoints.add(
                    new Endpoint(
                            name,
                            version,
                            requestMethod,
                            fullPath,
                            returnType,
                            expectsBody,
                            urlParameters
                    )
            );
        }
    }

    @VisibleForTesting
    String getRequestMethod(Method method) {
        String requestMethod = "";
        Iterator<Class<? extends Annotation>> mappingAnnotations = Arrays.asList(GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class).iterator();

        while (requestMethod.isEmpty() && mappingAnnotations.hasNext()) {
            Class<? extends Annotation> mappingAnnotation = mappingAnnotations.next();
            Annotation mapping = method.getAnnotation(mappingAnnotation);
            Optional<String> optional = getRequestMappingMethod(mapping);
            if (optional.isPresent()) {
                requestMethod = optional.get();
            }
        }

        return requestMethod;
    }

    @VisibleForTesting
    String getRequestMethodName(Method method) {
        String name = method.getName();

        if (endpoints.stream().noneMatch(e -> e.getName().equals(name))) {
            return name;
        } else {
            throw new ConfigurationException("Duplicate endpoint method '" + name + "'");
        }
    }

    @VisibleForTesting
    Optional<String> getRequestMappingMethod(Annotation annotation) {
        Optional<String> method;

        if (annotation != null) {
            RequestMapping requestMapping = annotation.annotationType().getAnnotation(RequestMapping.class);

            if (requestMapping != null) {
                method = Optional.of(requestMapping.method()[0].toString());
            } else {
                method = Optional.empty();
            }
        } else {
            method = Optional.empty();
        }

        return method;
    }

    @VisibleForTesting
    String getRequestPath(Method method, String requestMethod) {
        String[] pathValues = getMappingValues(method, requestMethod);
        return (pathValues.length > 0) ? pathValues[0] : "";
    }

    @VisibleForTesting
    String[] getMappingValues(Method method, String requestMethod) {
        String[] pathValues;

        switch (requestMethod) {
            case "GET":
                pathValues = method.getAnnotation(GetMapping.class).value();
                break;
            case "POST":
                pathValues = method.getAnnotation(PostMapping.class).value();
                break;
            case "PUT":
                pathValues = method.getAnnotation(PutMapping.class).value();
                break;
            case "DELETE":
                pathValues = method.getAnnotation(DeleteMapping.class).value();
                break;
            default:
                throw new ConfigurationException("Invalid request method for endpoint '" + method.getName() + "'");
        }

        return pathValues;
    }

    @VisibleForTesting
    String getRequestReturnType(Method method) {
        return method.getReturnType().getSimpleName();
    }

    @VisibleForTesting
    boolean containsRequestBody(Method method) {
        boolean expectsBody = false;
        Iterator<Parameter> parameters = Arrays.asList(method.getParameters()).listIterator();

        while (!expectsBody && parameters.hasNext()) {
            RequestBody requestBody = parameters.next().getAnnotation(RequestBody.class);
            expectsBody = requestBody != null;
        }

        return expectsBody;
    }

    @VisibleForTesting
    String getControllerPath(Class<?> controllerInterface) {
        String path = "";
        RequestMapping requestMapping = controllerInterface.getAnnotation(RequestMapping.class);

        if (requestMapping != null) {
            path = requestMapping.value()[0];
        }

        return path;
    }

    @VisibleForTesting
    int getControllerPathVersion(String path) {
        int version = 0;

        Iterator<String> bits = Arrays.asList(path.split("/")).iterator();

        while (version < 1 && bits.hasNext()) {
            String dir = bits.next();

            if (dir.startsWith("v")) {
                Optional<Integer> optional = NumberUtils.parseInt(dir.substring(1));

                if (optional.isPresent()) {
                    version = optional.get();
                }
            }
        }

        return version;
    }

    @VisibleForTesting
    Set<UrlParameter> getUrlParameters(Method method) {
        Set<UrlParameter> urlParameters = new HashSet<>();

        for (Parameter parameter : method.getParameters()) {
            addUrlParameter(parameter, urlParameters);
        }

        return urlParameters;
    }

    @VisibleForTesting
    void addUrlParameter(Parameter parameter, Set<UrlParameter> urlParameters) {
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);

        if (requestParam != null) {
            String name = requestParam.value().isEmpty() ? parameter.getName() : requestParam.value();
            String type = parameter.getType().getSimpleName();
            boolean optional = !requestParam.required();
            urlParameters.add(new UrlParameter(name, type, optional));
        }
    }

    @Override
    public Set<Endpoint> getEndpoints() {
        return endpoints;
    }

}

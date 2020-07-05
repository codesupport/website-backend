package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.models.AuthenticationRequest;
import org.junit.Test;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AuthenticationControllerTest {

    @Test
    public void shouldBeAnnotatedWithRestController() {
        RestController annotation = AuthenticationController.class.getAnnotation(RestController.class);

        assertNotNull(annotation);
    }

    @Test
    public void shouldBeAnnotatedWithValidated() {
        Validated annotation = AuthenticationController.class.getAnnotation(Validated.class);

        assertNotNull(annotation);
    }

    @Test
    public void shouldBeAnnotatedWithRequestMapping() {
        RequestMapping annotation = AuthenticationController.class.getAnnotation(RequestMapping.class);
        List<String> actual;
        List<String> expected = Collections.singletonList("/authenticate");

        if (annotation != null) {
            actual = Arrays.asList(annotation.value());
        } else {
            actual = Collections.emptyList();
            fail("Annotation missing");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void shouldBeAnnotatedWithPostMappingOnAuthenticate() throws NoSuchMethodException {
        Method method = AuthenticationController.class.getMethod("authenticate", AuthenticationRequest.class);
        PostMapping annotation = method.getAnnotation(PostMapping.class);

        assertNotNull(annotation);
    }

    @Test
    public void shouldBeAnnotatedWithGetMappingOnRefreshToken() throws NoSuchMethodException {
        Method method = AuthenticationController.class.getMethod("refreshToken");
        List<String> expected = Collections.singletonList("/refresh");
        List<String> actual;

        GetMapping annotation = method.getAnnotation(GetMapping.class);

        if (annotation != null) {
            actual = Arrays.asList(annotation.value());
        } else {
            actual = Collections.emptyList();
            fail("Annotation missing");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void shouldBeAnnotatedWithGetMappingOnLinkDiscord() throws NoSuchMethodException {
        Method method = AuthenticationController.class.getMethod("linkDiscord", String.class);
        List<String> expected = Collections.singletonList("/discord");
        List<String> actual;

        GetMapping annotation = method.getAnnotation(GetMapping.class);

        if (annotation != null) {
            actual = Arrays.asList(annotation.value());
        } else {
            actual = Collections.emptyList();
            fail("Annotation missing");
        }

        assertEquals(expected, actual);
    }

}

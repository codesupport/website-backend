package dev.codesupport.web.common.security;

import org.junit.Test;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class AuthenticationServiceTest {

    @Test
    public void shouldBeAnnotatedWithService() {
        Service annotation = AuthenticationService.class.getAnnotation(Service.class);

        assertNotNull(annotation);
    }

    @Test
    public void shouldNotBeAnnotatedWithPreAuthorizeOnCreateTokenForEmailAndPassword() throws NoSuchMethodException {
        Method method = AuthenticationService.class.getMethod("authenticate", String.class, String.class);
        Service annotation = method.getAnnotation(Service.class);

        assertNull(annotation);
    }

    @Test
    public void shouldNotBeAnnotatedWithPreAuthorizeOnGetUserDetailsByEmail() throws NoSuchMethodException {
        Method method = AuthenticationService.class.getMethod("getUserByToken", String.class);
        Service annotation = method.getAnnotation(Service.class);

        assertNull(annotation);
    }

    @Test
    public void shouldBeAnnotatedWithPreAuthorizeOnLinkDiscord() throws NoSuchMethodException {
        Method method = AuthenticationService.class.getMethod("linkDiscord", String.class);
        String expected = "hasPermission('discord', 'link')";
        String actual;

        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);

        if (annotation != null) {
            actual = annotation.value();
        } else {
            actual = null;
            fail("Annotation missing");
        }

        assertEquals(expected, actual);
    }

}

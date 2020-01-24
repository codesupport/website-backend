package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.Accessor;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class LinkAccountEvaluatorTest {

    @Test
    public void shouldReturnFalseForHasPermissionIfNullAuth() {
        LinkAccountEvaluator evaluator = new LinkAccountEvaluator();

        assertFalse(
                evaluator.hasPermission(null, "", "")
        );
    }

    @Test
    public void shouldReturnFalseForHasPermissionIfAnonAuth() {
        LinkAccountEvaluator evaluator = new LinkAccountEvaluator();

        Authentication mockAuthentication = mock(AnonymousAuthenticationToken.class);

        assertFalse(
                evaluator.hasPermission(mockAuthentication, "", "")
        );
    }

    @Test
    public void shouldReturnTrueIfValidAuth() {
        LinkAccountEvaluator evaluator = new LinkAccountEvaluator();

        Authentication mockAuthentication = mock(UsernamePasswordAuthenticationToken.class);

        assertTrue(
                evaluator.hasPermission(mockAuthentication, "", "")
        );
    }

    @Test
    public void shouldGetCorrectAccessor() {
        LinkAccountEvaluator evaluator = new LinkAccountEvaluator();

        assertEquals(Accessor.DISCORD, evaluator.getAccessor());
    }

}

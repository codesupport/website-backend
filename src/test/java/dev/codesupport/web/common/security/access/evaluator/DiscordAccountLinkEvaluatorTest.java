package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class DiscordAccountLinkEvaluatorTest {

    @Test
    public void shouldSetPermissionToLink() {
        DiscordAccountLinkEvaluator evaluator = new DiscordAccountLinkEvaluator();

        Permission actual = (Permission) ReflectionTestUtils.getField(
                evaluator,
                "permission"
        );

        assertEquals(Permission.LINK, actual);
    }

    @Test
    public void shouldReturnFalseForHasPermissionIfNullAuth() {
        DiscordAccountLinkEvaluator evaluator = new DiscordAccountLinkEvaluator();

        assertFalse(
                evaluator.hasPermission(null, "")
        );
    }

    @Test
    public void shouldReturnFalseForHasPermissionIfAnonAuth() {
        DiscordAccountLinkEvaluator evaluator = new DiscordAccountLinkEvaluator();

        Authentication mockAuthentication = mock(AnonymousAuthenticationToken.class);

        assertFalse(
                evaluator.hasPermission(mockAuthentication, "")
        );
    }

    @Test
    public void shouldReturnTrueIfValidAuth() {
        DiscordAccountLinkEvaluator evaluator = new DiscordAccountLinkEvaluator();

        Authentication mockAuthentication = mock(UsernamePasswordAuthenticationToken.class);

        assertTrue(
                evaluator.hasPermission(mockAuthentication, "")
        );
    }

    @Test
    public void shouldGetCorrectAccessor() {
        DiscordAccountLinkEvaluator evaluator = new DiscordAccountLinkEvaluator();

        assertEquals(Accessor.DISCORD, evaluator.getAccessor());
    }

}

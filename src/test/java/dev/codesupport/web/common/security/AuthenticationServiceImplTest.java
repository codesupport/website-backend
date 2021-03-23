package dev.codesupport.web.common.security;

import com.google.common.net.HttpHeaders;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.configuration.DiscordAppProperties;
import dev.codesupport.web.common.configuration.HttpSessionProperties;
import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenResponse;
import dev.codesupport.web.common.security.models.DiscordUser;
import dev.codesupport.web.common.security.models.UserDetails;
import dev.codesupport.web.common.service.http.client.HttpClient;
import dev.codesupport.web.common.service.http.client.HttpMethod;
import dev.codesupport.web.common.service.http.client.RestRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//S1192 - DRY is not applicable to unit tests.
//S2068 - This is not a real password
@SuppressWarnings({"squid:S1192", "squid:S2068"})
public class AuthenticationServiceImplTest {

    private static AuthenticationServiceImpl serviceSpy;

    private static HttpSessionProperties mockHttpSessionProperties;

    private static DiscordAppProperties mockDiscordAppProperties;

    private static UserRepository mockUserRepository;

    private static HashingUtility mockHashingUtility;

    private static HttpClient mockHttpClient;

    @BeforeClass
    public static void init() {
        mockHttpSessionProperties = mock(HttpSessionProperties.class);
        mockDiscordAppProperties = mock(DiscordAppProperties.class);
        mockUserRepository = mock(UserRepository.class);
        mockHashingUtility = mock(HashingUtility.class);
        mockHttpClient = mock(HttpClient.class);
    }

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(null);

        Mockito.reset(
                mockHttpSessionProperties,
                mockDiscordAppProperties,
                mockUserRepository,
                mockHashingUtility,
                mockHttpClient
        );

        serviceSpy = spy(
                new AuthenticationServiceImpl(
                        mockHttpSessionProperties,
                        mockDiscordAppProperties,
                        mockUserRepository,
                        mockHashingUtility,
                        mockHttpClient
                )
        );

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn("http://www.discordapp.com")
                .when(mockDiscordAppProperties)
                .getApiHost();

        serviceSpy.init();
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionForCreateTokenForValidEmailAndPasswordIfUserDisabled() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";

        UserBuilder builder = UserBuilder.builder()
                .alias(alias)
                .email(email)
                .hashPassword("abc123")
                .disabled(true);

        UserEntity userEntity = builder.buildEntity();

        doReturn(userEntity)
                .when(serviceSpy)
                .getUserByEmail(email);

        doReturn(true)
                .when(mockHashingUtility)
                .verifyPassword(password, userEntity.getHashPassword());

        serviceSpy.authenticate(email, password);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfInvalidPassword() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";

        UserBuilder builder = UserBuilder.builder()
                .alias(alias)
                .email(email)
                .hashPassword("abc123")
                .disabled(false);

        UserEntity userEntity = builder.buildEntity();

        Optional<UserEntity> optional = Optional.of(userEntity);

        doReturn(optional)
                .when(mockUserRepository)
                .findByEmailIgnoreCase(email);

        doReturn(false)
                .when(mockHashingUtility)
                .verifyPassword(password, userEntity.getHashPassword());

        serviceSpy.authenticate(email, password);
    }

    @Test
    public void shouldCreateTokenForValidEmailAndPassword() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String expected = "abcdefg";

        UserBuilder builder = UserBuilder.builder()
                .alias(alias)
                .email(email)
                .hashPassword("abc123")
                .disabled(false);

        UserEntity userEntity = builder.buildEntity();

        doReturn(userEntity)
                .when(serviceSpy)
                .getUserByEmail(email);

        doReturn(true)
                .when(mockHashingUtility)
                .verifyPassword(password, userEntity.getHashPassword());

        doReturn(expected)
                .when(serviceSpy)
                .createTokenCookieForUser(userEntity);

        String actual = serviceSpy.authenticate(email, password);

        assertSame(expected, actual);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfUserDoesNotExist() {
        String email = "email@email.em";

        Optional<UserEntity> optional = Optional.empty();

        doReturn(optional)
                .when(mockUserRepository)
                .findByEmailIgnoreCase(email);

        serviceSpy.getUserByEmail(email);
    }

    @Test
    public void shouldGetUserByToken() {
        String token = "tokentoken";

        Optional<UserEntity> expected = Optional.of(mock(UserEntity.class));

        doReturn(expected)
                .when(mockUserRepository)
                .findByAccessTokenIgnoreCase(token);

        Optional<UserEntity> actual = serviceSpy.getUserByToken(token);

        assertSame(expected, actual);
    }

    @Test
    public void shouldSaveTokenCookieForUser() {
        String cookieName = "name";
        int cookieMaxAge = 5;

        HttpSessionProperties.CookieConfiguration mockCookieConfig = mock(HttpSessionProperties.CookieConfiguration.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(cookieMaxAge)
                .when(mockCookieConfig)
                .getMaxAge();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(cookieName)
                .when(mockCookieConfig)
                .getName();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(mockCookieConfig)
                .when(mockHttpSessionProperties)
                .getCookie();

        doReturn(null)
                .when(mockUserRepository)
                .save(any());

        UserEntity userEntity = new UserEntity();

        serviceSpy.createTokenCookieForUser(userEntity);

        verify(mockUserRepository, times(1))
                .save(userEntity);
    }

    @Test
    public void shouldReturnStringFromCreateTokenCookieForUser() {
        String cookieName = "name";
        int cookieMaxAge = 5;

        HttpSessionProperties.CookieConfiguration mockCookieConfig = mock(HttpSessionProperties.CookieConfiguration.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(cookieMaxAge)
                .when(mockCookieConfig)
                .getMaxAge();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(cookieName)
                .when(mockCookieConfig)
                .getName();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(mockCookieConfig)
                .when(mockHttpSessionProperties)
                .getCookie();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(true)
                .when(mockCookieConfig)
                .isSecure();

        doReturn(null)
                .when(mockUserRepository)
                .save(any());

        UserEntity userEntity = new UserEntity();

        String actual = serviceSpy.createTokenCookieForUser(userEntity);
        String expected = cookieName + "=" + userEntity.getAccessToken() + "; Secure; HttpOnly; Path=/; MaxAge=" + cookieMaxAge;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyLinkUserDiscordAccount() {
        String code = "myCode";
        String email = "user@email.com";
        String token = "myToken";

        doReturn(email)
                .when(serviceSpy)
                .getUserEmailFromAuthentication();

        DiscordOAuthTokenResponse mockResponse = mock(DiscordOAuthTokenResponse.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(token)
                .when(mockResponse)
                .getAccessToken();

        doReturn(mockResponse)
                .when(serviceSpy)
                .getTokenFromDiscordApi(code);

        DiscordUser mockDiscordUser = mock(DiscordUser.class);

        doReturn(mockDiscordUser)
                .when(serviceSpy)
                .getDiscordUserDetailsFromDiscordApi(token);

        doNothing()
                .when(serviceSpy)
                .saveDiscordIdToUser(any(), any());

        serviceSpy.linkDiscord(code);

        verify(serviceSpy, times(1))
                .saveDiscordIdToUser(email, mockDiscordUser);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfUserDoesNotExistWithEmail() {
        String email = "user@email.com";

        Optional<UserEntity> optional = Optional.empty();

        doReturn(optional)
                .when(mockUserRepository)
                .findByEmailIgnoreCase(email);

        doReturn(null)
                .when(mockUserRepository)
                .save(any());

        DiscordUser mockDiscordUser = mock(DiscordUser.class);

        serviceSpy.saveDiscordIdToUser(email, mockDiscordUser);
    }

    @Test
    public void shouldSaveDiscordIdToUser() {
        String email = "user@email.com";
        String id = "discordid";
        String discordUser = "user";
        String discordDiscriminator = "5455";

        UserEntity expectedUserEntity = new UserEntity();
        expectedUserEntity.setEmail(email);
        expectedUserEntity.setDiscordId(id);
        expectedUserEntity.setDiscordUsername(discordUser + "#" + discordDiscriminator);

        UserEntity userEntity = UserBuilder.builder()
                .email(email)
                .buildEntity();

        Optional<UserEntity> optional = Optional.of(userEntity);

        doReturn(optional)
                .when(mockUserRepository)
                .findByEmailIgnoreCase(email);

        doReturn(null)
                .when(mockUserRepository)
                .save(any());

        DiscordUser mockDiscordUser = mock(DiscordUser.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(mockDiscordUser)
                .getId();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(discordUser)
                .when(mockDiscordUser)
                .getUsername();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(discordDiscriminator)
                .when(mockDiscordUser)
                .getDiscriminator();

        serviceSpy.saveDiscordIdToUser(email, mockDiscordUser);

        verify(mockUserRepository, times(1))
                .save(expectedUserEntity);
    }

    @Test
    @Ignore // need to fix this test
    public void shouldSendCorrectHttpRequestToDiscordApiForUserDetails() {
        String accessToken = "myToken";

        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        RestRequest<String, DiscordUser> restRequestSpy = spy(RestRequest.class);
        ReflectionTestUtils.setField(restRequestSpy, "requestClass", String.class);
        ReflectionTestUtils.setField(restRequestSpy, "responseClass", DiscordUser.class);

        doReturn(new DiscordUser())
                .when(restRequestSpy)
                .sync(HttpMethod.POST);

        doReturn(restRequestSpy)
                .when(mockHttpClient)
                .rest(String.class, DiscordUser.class);

        serviceSpy.getDiscordUserDetailsFromDiscordApi(accessToken);

        String discordOAuthTokenUri = "https://discordapp.com/api/users/@me";

        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        httpHeaders.put(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        RestRequest<String, DiscordUser> expectedSpy = spy(RestRequest.class);
        ReflectionTestUtils.setField(expectedSpy, "url", discordOAuthTokenUri);
        ReflectionTestUtils.setField(expectedSpy, "headers", httpHeaders);
        ReflectionTestUtils.setField(expectedSpy, "requestClass", String.class);
        ReflectionTestUtils.setField(expectedSpy, "responseClass", DiscordUser.class);

        assertEquals(expectedSpy.toString(), restRequestSpy.toString());
    }

    @Test
    @Ignore // Need to fix this test
    public void shouldSendCorrectHttpRequestToDiscordApiForToken() {
        String code = "mycode";

        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        RestRequest<DiscordOAuthTokenRequest, DiscordOAuthTokenResponse> restRequestSpy = spy(RestRequest.class);
        ReflectionTestUtils.setField(restRequestSpy, "requestClass", DiscordOAuthTokenRequest.class);
        ReflectionTestUtils.setField(restRequestSpy, "responseClass", DiscordOAuthTokenResponse.class);

        doReturn(new DiscordOAuthTokenResponse())
                .when(restRequestSpy)
                .sync(HttpMethod.POST);

        doReturn(restRequestSpy)
                .when(mockHttpClient)
                .rest(DiscordOAuthTokenRequest.class, DiscordOAuthTokenResponse.class);

        serviceSpy.getTokenFromDiscordApi(code);

        String discordOAuthTokenUri = "https://discordapp.com/api/oauth2/token";

        DiscordOAuthTokenRequest tokenRequest = null;//DiscordOAuthTokenRequest.create(code);

        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        httpHeaders.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        RestRequest<DiscordOAuthTokenRequest, DiscordOAuthTokenResponse> expectedSpy = spy(RestRequest.class);
        ReflectionTestUtils.setField(expectedSpy, "url", discordOAuthTokenUri);
        ReflectionTestUtils.setField(expectedSpy, "headers", httpHeaders);
        ReflectionTestUtils.setField(expectedSpy, "requestObject", tokenRequest);
        ReflectionTestUtils.setField(expectedSpy, "requestClass", DiscordOAuthTokenRequest.class);
        ReflectionTestUtils.setField(expectedSpy, "responseClass", DiscordOAuthTokenResponse.class);

        assertEquals(expectedSpy.toString(), restRequestSpy.toString());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfAuthenticationHasNoUserDetails() {
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken(
                        "user",
                        "principle",
                        Collections.singletonList(new SimpleGrantedAuthority("ANONYMOUS_USER"))
                )
        );

        serviceSpy.getUserEmailFromAuthentication();
    }

    @Test
    public void shouldCorrectGetUserEmailFromSecurityContextAuthorization() {
        String expected = "my@email.com";

        UserDetails mockUserDetails = mock(UserDetails.class);

        doReturn(Collections.emptyList())
                .when(mockUserDetails)
                .getAuthorities();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(expected)
                .when(mockUserDetails)
                .getEmail();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        mockUserDetails, null, mockUserDetails.getAuthorities()
                )
        );

        String actual = serviceSpy.getUserEmailFromAuthentication();

        assertEquals(expected, actual);
    }

}

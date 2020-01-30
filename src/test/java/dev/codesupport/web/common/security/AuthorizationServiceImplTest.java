package dev.codesupport.web.common.security;

import com.google.common.net.HttpHeaders;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.jwt.JwtUtility;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenResponse;
import dev.codesupport.web.common.security.models.DiscordUser;
import dev.codesupport.web.common.security.models.UserDetails;
import dev.codesupport.web.common.service.http.HttpClient;
import dev.codesupport.web.common.service.http.HttpMethod;
import dev.codesupport.web.common.service.http.RestRequest;
import org.junit.Before;
import org.junit.BeforeClass;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//S1192 - DRY is not applicable to unit tests.
//S2068 - This is not a real password
@SuppressWarnings({"squid:S1192", "squid:S2068"})
public class AuthorizationServiceImplTest {

    private static AuthorizationServiceImpl spyService;

    private static UserRepository mockUserRepository;

    private static HashingUtility mockHashingUtility;

    private static JwtUtility mockJwtUtility;

    private static HttpClient mockHttpClient;

    @BeforeClass
    public static void init() {
        mockUserRepository = mock(UserRepository.class);
        mockHashingUtility = mock(HashingUtility.class);
        mockJwtUtility = mock(JwtUtility.class);
        mockHttpClient = mock(HttpClient.class);

        spyService = spy(new AuthorizationServiceImpl(mockUserRepository, mockHashingUtility, mockJwtUtility, mockHttpClient));
    }

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(null);

        Mockito.reset(
                mockUserRepository,
                mockHashingUtility,
                mockJwtUtility,
                mockHttpClient,
                spyService
        );
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfUserDisabled() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String token = "tokentokentoken";

        UserDetails userDetails = new UserDetails(alias, password, email, Collections.emptySet(), true);

        doReturn(userDetails)
                .when(spyService)
                .getUserDetailsByEmail(email);

        doReturn(true)
                .when(mockHashingUtility)
                .verifyPassword(password, password);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(
                        alias,
                        email
                );

        spyService.createTokenForEmailAndPassword(email, password);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfInvalidPassword() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String token = "tokentokentoken";

        UserDetails userDetails = new UserDetails(alias, password, email, Collections.emptySet(), false);

        doReturn(userDetails)
                .when(spyService)
                .getUserDetailsByEmail(email);

        doReturn(false)
                .when(mockHashingUtility)
                .verifyPassword(password, password);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(
                        alias,
                        email
                );

        spyService.createTokenForEmailAndPassword(email, password);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfGetUserDetailsThrowsInvalidUserException() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String token = "tokentokentoken";

        doThrow(InvalidUserException.class)
                .when(spyService)
                .getUserDetailsByEmail(email);

        doReturn(true)
                .when(mockHashingUtility)
                .verifyPassword(password, password);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(
                        alias,
                        email
                );

        spyService.createTokenForEmailAndPassword(email, password);
    }

    @Test
    public void shouldCreateTokenForValidEmailAndPassword() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";
        String token = "tokentokentoken";

        UserDetails userDetails = new UserDetails(alias, password, email, Collections.emptySet(), false);

        doReturn(userDetails)
                .when(spyService)
                .getUserDetailsByEmail(email);

        doReturn(true)
                .when(mockHashingUtility)
                .verifyPassword(password, password);

        doReturn(token)
                .when(mockJwtUtility)
                .generateToken(
                        alias,
                        email
                );

        String actual = spyService.createTokenForEmailAndPassword(email, password);

        assertEquals(token, actual);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfUserNotFound() {
        String email = "email@email.em";

        doReturn(null)
                .when(mockUserRepository)
                .findByEmail(email);

        spyService.getUserDetailsByEmail(email);
    }

    @Test
    public void shouldReturnCorrectUserDetailsIfUserFound() {
        String alias = "me";
        String email = "email@email.em";
        String password = "mypassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setAlias(alias);
        userEntity.setEmail(email);
        userEntity.setHashPassword(password);
        userEntity.setPermission(Collections.emptySet());
        userEntity.setDisabled(false);

        doReturn(userEntity)
                .when(mockUserRepository)
                .findByEmail(email);

        UserDetails expected = new UserDetails(alias, password, email, Collections.emptySet(), false);

        UserDetails actual = spyService.getUserDetailsByEmail(email);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyLinkUserDiscordAccount() {
        String code = "myCode";
        String email = "user@email.com";
        String token = "myToken";

        UserRepository mockRepository = mock(UserRepository.class);
        HashingUtility mockHashingUtility = mock(HashingUtility.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        HttpClient mockHttpClient = mock(HttpClient.class);

        AuthorizationServiceImpl serviceSpy = spy(new AuthorizationServiceImpl(mockRepository, mockHashingUtility, mockJwtUtility, mockHttpClient));

        doReturn(email)
                .when(serviceSpy)
                .getUserEmailFromAuthorization();

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

    @Test
    public void shouldSaveDiscordIdToUser() {
        String email = "user@email.com";
        String id = "discordid";
        String discordUser = "user";
        String discordDiscriminator = "5455";

        UserRepository mockRepository = mock(UserRepository.class);
        HashingUtility mockHashingUtility = mock(HashingUtility.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        HttpClient mockHttpClient = mock(HttpClient.class);

        UserEntity expectedUserEntity = new UserEntity();
        expectedUserEntity.setDiscordId(id);
        expectedUserEntity.setDiscordUsername(discordUser + "#" + discordDiscriminator);

        UserEntity userEntity = new UserEntity();

        doReturn(userEntity)
                .when(mockRepository)
                .findByEmail(email);

        doReturn(new UserEntity())
                .when(mockRepository)
                .save(expectedUserEntity);

        AuthorizationServiceImpl service = new AuthorizationServiceImpl(mockRepository, mockHashingUtility, mockJwtUtility, mockHttpClient);

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

        service.saveDiscordIdToUser(email, mockDiscordUser);

        verify(mockRepository, times(1))
                .save(expectedUserEntity);
    }

    @Test
    public void shouldSendCorrectHttpRequestToDiscordApiForUserDetails() {
        String accessToken = "myToken";

        UserRepository mockRepository = mock(UserRepository.class);
        HashingUtility mockHashingUtility = mock(HashingUtility.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        HttpClient mockHttpClient = mock(HttpClient.class);

        AuthorizationServiceImpl service = new AuthorizationServiceImpl(mockRepository, mockHashingUtility, mockJwtUtility, mockHttpClient);

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

        service.getDiscordUserDetailsFromDiscordApi(accessToken);

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
    public void shouldSendCorrectHttpRequestToDiscordApiForToken() {
        String code = "mycode";

        UserRepository mockRepository = mock(UserRepository.class);
        HashingUtility mockHashingUtility = mock(HashingUtility.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        HttpClient mockHttpClient = mock(HttpClient.class);

        AuthorizationServiceImpl service = new AuthorizationServiceImpl(mockRepository, mockHashingUtility, mockJwtUtility, mockHttpClient);

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

        service.getTokenFromDiscordApi(code);

        String discordOAuthTokenUri = "https://discordapp.com/api/oauth2/token";

        DiscordOAuthTokenRequest tokenRequest = DiscordOAuthTokenRequest.create(code);

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
        UserRepository mockRepository = mock(UserRepository.class);
        HashingUtility mockHashingUtility = mock(HashingUtility.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        HttpClient mockHttpClient = mock(HttpClient.class);

        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken(
                        "user",
                        "principle",
                        Collections.singletonList(new SimpleGrantedAuthority("ANONYMOUS_USER"))
                )
        );

        AuthorizationServiceImpl service = new AuthorizationServiceImpl(mockRepository, mockHashingUtility, mockJwtUtility, mockHttpClient);

        service.getUserEmailFromAuthorization();
    }

    @Test
    public void shouldCorrectGetUserEmailFromSecurityContextAuthorization() {
        String expected = "my@email.com";

        UserRepository mockRepository = mock(UserRepository.class);
        HashingUtility mockHashingUtility = mock(HashingUtility.class);
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        HttpClient mockHttpClient = mock(HttpClient.class);

        AuthorizationServiceImpl service = new AuthorizationServiceImpl(mockRepository, mockHashingUtility, mockJwtUtility, mockHttpClient);

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

        String actual = service.getUserEmailFromAuthorization();

        assertEquals(expected, actual);
    }

}

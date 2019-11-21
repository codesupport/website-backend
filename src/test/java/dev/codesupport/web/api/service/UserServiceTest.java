package dev.codesupport.web.api.service;

import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class UserServiceTest {

    private static UserService service;

    private static CrudOperations<UserEntity, Long, User> mockCrudOperations;

    private static List<User> getUserList;

    @BeforeClass
    public static void init() {
        //unchecked - this is fine for a unit test
        //noinspection unchecked
        mockCrudOperations = mock(CrudOperations.class);

        ApplicationContext mockContext = mock(ApplicationContext.class);

        ReflectionTestUtils.setField(mockCrudOperations, "context", mockContext);

        UserRepository mockUserRepository = mock(UserRepository.class);

        service = new UserService(mockUserRepository);
        ReflectionTestUtils.setField(service, "userCrudOperations", mockCrudOperations);

        List<UserBuilder> userBuilders = Collections.singletonList(
                UserBuilder.builder()
                        .id(1L)
                        .username("billy")
                        .password("123")
                        .email("bill@bob.com")
                        .addedBy(2L)
                        .avatarLink("bill.jpg")
                        .joinDate(1L)
        );

        getUserList = userBuilders.stream().map(UserBuilder::buildDomain).collect(Collectors.toList());
    }

    @Before
    public void setup() {
        //unchecked - this is fine for a unit test
        //noinspection unchecked
        Mockito.reset(
                mockCrudOperations
        );
    }

    @Test
    public void shouldReturnCorrectUsersWithFindAllUsers() {
        doReturn(getUserList)
                .when(mockCrudOperations)
                .getAll();

        List<User> actual = service.findAllUsers();

        assertEquals(getUserList, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithGetUserById() {
        Long id = 1L;

        doReturn(getUserList)
                .when(mockCrudOperations)
                .getById(id);

        List<User> actual = service.getUserById(id);

        assertEquals(getUserList, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithCreateUsers() {
        doReturn(getUserList)
                .when(mockCrudOperations)
                .createEntities(getUserList);

        List<User> actual = service.createUsers(getUserList);

        assertEquals(getUserList, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithUpdateUsers() {
        doReturn(getUserList)
                .when(mockCrudOperations)
                .updateEntities(getUserList);

        List<User> actual = service.updateUsers(getUserList);

        assertEquals(getUserList, actual);
    }

    @Test
    public void shouldReturnCorrectUsersWithDeleteUsers() {
        doNothing()
                .when(mockCrudOperations)
                .deleteEntities(getUserList);

        List<User> actual = service.deleteUsers(getUserList);

        assertEquals(Collections.emptyList(), actual);
    }
}

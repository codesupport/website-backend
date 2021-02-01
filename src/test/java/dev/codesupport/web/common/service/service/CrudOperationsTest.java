package dev.codesupport.web.common.service.service;

import dev.codesupport.testutils.domain.MockIdentifiableDomain;
import dev.codesupport.testutils.entity.MockIdentifiableEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CrudOperationsTest {

    private static CrudOperations<MockIdentifiableEntity, MockIdentifiableDomain, Long> crudOperationsSpy;
    private static CrudRepository<MockIdentifiableEntity, Long> mockRepository;
    private static ApplicationContext mockContext;

    @BeforeClass
    public static void init() {
        mockContext = mock(ApplicationContext.class);

        //unchecked - This is not a concern for the purposes of this test.
        //noinspection unchecked
        mockRepository = mock(CrudRepository.class);

        CrudOperations.setContext(mockContext);

        crudOperationsSpy = spy(
                new CrudOperations<>(
                        mockRepository,
                        MockIdentifiableEntity.class,
                        MockIdentifiableDomain.class
                )
        );
    }

    @Before
    public void setUp() {
        CrudOperations.setContext(mockContext);

        Mockito.reset(
                crudOperationsSpy,
                mockRepository
        );
    }

    @Test
    public void shouldGetCorrectEntityById() {
        long id = 1L;
        String value = "value";

        MockIdentifiableEntity entity = new MockIdentifiableEntity(id, value);

        doReturn(entity)
                .when(mockRepository)
                .getById(id);

        doNothing()
                .when(crudOperationsSpy)
                .preGetEntities(Collections.singletonList(entity));

        MockIdentifiableDomain expected = new MockIdentifiableDomain(entity.getId(), entity.getPropertyA());
        MockIdentifiableDomain actual = crudOperationsSpy.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyGetAllEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToReturn = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableEntity> returnedEntities = Collections.singletonList(
                new MockIdentifiableEntity(id, value)
        );

        doReturn(returnedEntities)
                .when(mockRepository)
                .findAll();

        List<MockIdentifiableDomain> actual = crudOperationsSpy.getAll();

        assertEquals(domainsToReturn, actual);
    }

    @Test
    public void shouldCallCreateEntitiesOnCreateEntity() {
        long id = 1L;
        String value = "value";

        MockIdentifiableDomain domainToSave = new MockIdentifiableDomain(id, value);

        List<MockIdentifiableDomain> domainsToReturn = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .createEntities(Collections.singletonList(domainToSave));

        MockIdentifiableDomain expected = domainsToReturn.get(0);

        MockIdentifiableDomain actual = crudOperationsSpy.createEntity(domainToSave);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectListOfSavedDomainsOnCreateEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToSave = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableDomain> domainsToReturn = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesAlreadyExistCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        List<MockIdentifiableDomain> actual = crudOperationsSpy.createEntities(domainsToSave);

        assertEquals(domainsToReturn, actual);
    }

    @Test
    public void shouldInvokePreUpdate() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToSave = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableDomain> domainsToReturn = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .preUpdate(anyList());

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.updateEntities(domainsToSave);

        verify(crudOperationsSpy, times(1))
                .preUpdate(domainsToSave);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldBubbleResourceNotFoundExceptionFromResourcesDontExistCheckOnUpdateEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToSave = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableDomain> domainsToReturn = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        doThrow(ResourceNotFoundException.class)
                .when(crudOperationsSpy)
                .resourcesDoNotExistCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.updateEntities(domainsToSave);
    }

    @Test
    public void shouldReturnCorrectListOfSavedDomainsOnUpdateEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToSave = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableDomain> domainsToReturn = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .preUpdate(anyList());

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        List<MockIdentifiableDomain> actual = crudOperationsSpy.updateEntities(domainsToSave);

        assertEquals(domainsToReturn, actual);
    }

    @Test
    public void shouldReturnCorrectListOfSavedDomainsOnSaveEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToSave = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableEntity> entitiesToSave = Collections.singletonList(
                new MockIdentifiableEntity(id, value)
        );

        List<MockIdentifiableDomain> domainsToReturn = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableEntity> returnedEntities = Collections.singletonList(
                new MockIdentifiableEntity(id, value)
        );

        doReturn(null)
                .when(mockRepository)
                .saveAll(anyIterable());

        doReturn(returnedEntities)
                .when(mockRepository)
                .saveAll(entitiesToSave);

        doReturn(null)
                .when(mockRepository)
                .getById(anyLong());

        doReturn(returnedEntities.get(0))
                .when(mockRepository)
                .getById(returnedEntities.get(0).getId());

        List<MockIdentifiableDomain> actual = crudOperationsSpy.saveEntities(domainsToSave);

        assertEquals(domainsToReturn, actual);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldBubbleResourceNotFoundExceptionFromResourcesDoNotExistCheckOnDeleteEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToDelete = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesDoNotExistCheck(anyCollection());

        doThrow(ResourceNotFoundException.class)
                .when(crudOperationsSpy)
                .resourcesDoNotExistCheck(domainsToDelete);

        doNothing()
                .when(mockRepository)
                .deleteAll(anyCollection());

        crudOperationsSpy.deleteEntities(domainsToDelete);
    }

    @Test
    public void shouldInvokePreDelete() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToDelete = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .preDelete(anyList());

        doNothing()
                .when(mockRepository)
                .deleteAll(anyCollection());

        crudOperationsSpy.deleteEntities(domainsToDelete);

        verify(crudOperationsSpy, times(1))
                .preDelete(domainsToDelete);
    }

    @Test
    public void shouldInvokeResourceDoesntExistCheckForEachDomain() {
        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        List<MockIdentifiableDomain> MockIdentifiableDomains = Arrays.asList(
                MockIdentifiableDomain,
                MockIdentifiableDomain
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourceDoesNotExistCheck(MockIdentifiableDomain);

        crudOperationsSpy.resourcesDoNotExistCheck(MockIdentifiableDomains);

        verify(crudOperationsSpy, times(MockIdentifiableDomains.size()))
                .resourceDoesNotExistCheck(MockIdentifiableDomain);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldBubbleUpResourceNotFoundExceptionIfDomainDoesntExist() {
        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        List<MockIdentifiableDomain> MockIdentifiableDomains = Arrays.asList(
                MockIdentifiableDomain,
                MockIdentifiableDomain
        );

        //Returns issues on second check
        doNothing()
                .doThrow(ResourceNotFoundException.class)
                .when(crudOperationsSpy)
                .resourceDoesNotExistCheck(MockIdentifiableDomain);

        crudOperationsSpy.resourcesDoNotExistCheck(MockIdentifiableDomains);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionIfIdIsNull() {
        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(null)
                .when(MockIdentifiableDomain)
                .getId();

        crudOperationsSpy.resourceDoesNotExistCheck(MockIdentifiableDomain);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionIfNotFoundInRepo() {
        long id = 1L;

        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(MockIdentifiableDomain)
                .getId();

        doReturn(false)
                .when(mockRepository)
                .existsById(id);

        crudOperationsSpy.resourceDoesNotExistCheck(MockIdentifiableDomain);
    }

    //DefaultAnnotationParam - SonarQube requires an assertion, be explicit!
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldThrowNoExceptionWhenDomainExistsForResourceDoesntExistCheck() {
        long id = 1L;

        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(MockIdentifiableDomain)
                .getId();

        doReturn(true)
                .when(mockRepository)
                .existsById(id);

        crudOperationsSpy.resourceDoesNotExistCheck(MockIdentifiableDomain);
    }


    @Test
    public void shouldInvokeResourceAlreadyExistsCheckForEachDomain() {
        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        List<MockIdentifiableDomain> MockIdentifiableDomains = Arrays.asList(
                MockIdentifiableDomain,
                MockIdentifiableDomain
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourceDoesNotExistCheck(MockIdentifiableDomain);

        crudOperationsSpy.resourcesAlreadyExistCheck(MockIdentifiableDomains);

        verify(crudOperationsSpy, times(MockIdentifiableDomains.size()))
                .resourceAlreadyExistsCheck(MockIdentifiableDomain);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldBubbleUpServiceLayerExceptionIfDomainAlreadyExists() {
        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        List<MockIdentifiableDomain> MockIdentifiableDomains = Arrays.asList(
                MockIdentifiableDomain,
                MockIdentifiableDomain
        );

        //Returns issues on second check
        doNothing()
                .doThrow(ServiceLayerException.class)
                .when(crudOperationsSpy)
                .resourceAlreadyExistsCheck(MockIdentifiableDomain);

        crudOperationsSpy.resourcesAlreadyExistCheck(MockIdentifiableDomains);
    }

    @Test
    public void shouldNotInvokeRepoCheckIfIdIsNullForResourceAlreadyExistsCheck() {
        long id = 1L;

        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(null)
                .when(MockIdentifiableDomain)
                .getId();

        crudOperationsSpy.resourceAlreadyExistsCheck(MockIdentifiableDomain);

        verify(mockRepository, times(0))
                .existsById(id);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldThrowServiceLayerExceptionIfIdIsNotNullAndIsFoundInRepo() {
        long id = 1L;

        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(MockIdentifiableDomain)
                .getId();

        doReturn(true)
                .when(mockRepository)
                .existsById(id);

        crudOperationsSpy.resourceAlreadyExistsCheck(MockIdentifiableDomain);
    }

    //DefaultAnnotationParam - SonarQube requires an assertion, be explicit!
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldThrowNoExceptionWhenDomainDoesntExistForResourceAlreadyExistsCheck() {
        long id = 1L;

        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(MockIdentifiableDomain)
                .getId();

        doReturn(false)
                .when(mockRepository)
                .existsById(id);

        crudOperationsSpy.resourceAlreadyExistsCheck(MockIdentifiableDomain);
    }
}
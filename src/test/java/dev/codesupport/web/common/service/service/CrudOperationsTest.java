package dev.codesupport.web.common.service.service;

import dev.codesupport.testutils.domain.MockIdentifiableDomain;
import dev.codesupport.testutils.entity.MockIdentifiableEntity;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.exception.ValidationException;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CrudOperationsTest {

    private static CrudOperations<MockIdentifiableEntity, MockIdentifiableDomain, Long> crudOperationsSpy;
    private static JpaRepository<MockIdentifiableEntity, Long> mockRepository;
    private static AbstractPersistenceValidator<MockIdentifiableEntity, Long, MockIdentifiableDomain, JpaRepository<MockIdentifiableEntity, Long>> mockValidator;
    private static ApplicationContext mockContext;

    @BeforeClass
    public static void init() {
        mockContext = mock(ApplicationContext.class);

        //unchecked - This is not a concern for the purposes of this test.
        //noinspection unchecked
        mockValidator = mock(AbstractPersistenceValidator.class);

        //ResultOfMethodCallIgnored - Not calling a method, making a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(MockIdentifiableEntity.class)
                .when(mockValidator)
                .getEntityType();

        //rawtypes - This is fine for hte purposes of this test.
        //noinspection rawtypes
        Map<String, AbstractPersistenceValidator> abstractValidationMap = new HashMap<>();
        abstractValidationMap.put("mockValidator", mockValidator);

        doReturn(abstractValidationMap)
                .when(mockContext)
                .getBeansOfType(AbstractPersistenceValidator.class);

        //unchecked - This is not a concern for the purposes of this test.
        //noinspection unchecked
        mockRepository = mock(JpaRepository.class);

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
                mockRepository,
                mockValidator
        );
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionIfNoContextConfigured() {
        CrudOperations.setContext(null);
        CrudOperations<MockIdentifiableEntity, MockIdentifiableDomain, Long> crudOperations = new CrudOperations<>(
                mockRepository,
                MockIdentifiableEntity.class,
                MockIdentifiableDomain.class
        );

        crudOperations.setupValidationBean();
    }

    @Test
    public void shouldGetFirstCorrectValidationBean() {
        //ResultOfMethodCallIgnored - Not calling a method, making a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(MockIdentifiableEntity.class)
                .when(mockValidator)
                .getEntityType();

        crudOperationsSpy.setupValidationBean();

        //rawtypes - Fine for the purposes of this test.
        //noinspection rawtypes
        AbstractPersistenceValidator actual = (AbstractPersistenceValidator) ReflectionTestUtils.getField(crudOperationsSpy, "validation");

        assertEquals(mockValidator, actual);
    }

    @Test
    public void shouldGetCorrectEntityById() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToReturn = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        Optional<MockIdentifiableEntity> optional = Optional.of(
                new MockIdentifiableEntity(id, "value")
        );

        doReturn(optional)
                .when(mockRepository)
                .findById(id);

        MockIdentifiableDomain expected = domainsToReturn.get(0);
        MockIdentifiableDomain actual = crudOperationsSpy.getById(id);

        assertEquals(expected, actual);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionIfNotFoundById() {
        long id = 1L;

        doReturn(Optional.empty())
                .when(mockRepository)
                .findById(id);

        crudOperationsSpy.getById(id);
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
    public void shouldInvokeValidationCheckOnCreateEntities() {
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

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.createEntities(domainsToSave);

        verify(crudOperationsSpy, times(1))
                .validationCheck(domainsToSave);
    }

    @Test(expected = ValidationException.class)
    public void shouldBubbleValidationExceptionFromValidationCheckOnCreateEntities() {
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

        doThrow(ValidationException.class)
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.createEntities(domainsToSave);
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

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        List<MockIdentifiableDomain> actual = crudOperationsSpy.createEntities(domainsToSave);

        assertEquals(domainsToReturn, actual);
    }

    @Test
    public void shouldInvokeResourcesDontExistsCheckOnUpdateEntities() {
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
                .resourcesDontExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.updateEntities(domainsToSave);

        verify(crudOperationsSpy, times(1))
                .resourcesDontExistCheck(domainsToSave);
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
                .resourcesDontExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.updateEntities(domainsToSave);
    }

    @Test
    public void shouldInvokeValidationCheckOnUpdateEntities() {
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
                .resourcesDontExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.updateEntities(domainsToSave);

        verify(crudOperationsSpy, times(1))
                .validationCheck(domainsToSave);
    }

    @Test(expected = ValidationException.class)
    public void shouldBubbleValidationExceptionFromValidationCheckOnUpdateEntities() {
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
                .resourcesDontExistCheck(domainsToSave);

        doThrow(ValidationException.class)
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

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
                .resourcesDontExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(domainsToReturn)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        List<MockIdentifiableDomain> actual = crudOperationsSpy.updateEntities(domainsToSave);

        assertEquals(domainsToReturn, actual);
    }

    @Test
    public void shouldInvokeResourcesDontExistsCheckOnDeleteEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToDelete = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableEntity> entitiesToDelete = Collections.singletonList(
                new MockIdentifiableEntity(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesDontExistCheck(domainsToDelete);

        doNothing()
                .when(mockRepository)
                .deleteAll(entitiesToDelete);

        crudOperationsSpy.deleteEntities(domainsToDelete);

        verify(crudOperationsSpy, times(1))
                .resourcesDontExistCheck(domainsToDelete);
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

        doReturn(returnedEntities)
                .when(mockRepository)
                .saveAll(entitiesToSave);

        List<MockIdentifiableDomain> actual = crudOperationsSpy.saveEntities(domainsToSave);

        assertEquals(domainsToReturn, actual);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldBubbleResourceNotFoundExceptionFromResourcesDontExistCheckOnDeleteEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToDelete = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableEntity> entitiesToDelete = Collections.singletonList(
                new MockIdentifiableEntity(id, value)
        );

        doThrow(ResourceNotFoundException.class)
                .when(crudOperationsSpy)
                .resourcesDontExistCheck(domainsToDelete);

        doNothing()
                .when(mockRepository)
                .deleteAll(entitiesToDelete);

        crudOperationsSpy.deleteEntities(domainsToDelete);
    }

    //DefaultAnnotationParam - SonarQube requires an assertion, be explicit!
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldGenerateNoExceptionWhenSuccessfulOnDeleteEntities() {
        long id = 1L;
        String value = "value";

        List<MockIdentifiableDomain> domainsToDelete = Collections.singletonList(
                new MockIdentifiableDomain(id, value)
        );

        List<MockIdentifiableEntity> entitiesToDelete = Collections.singletonList(
                new MockIdentifiableEntity(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesDontExistCheck(domainsToDelete);

        doNothing()
                .when(mockRepository)
                .deleteAll(entitiesToDelete);

        crudOperationsSpy.deleteEntities(domainsToDelete);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowValidationExceptionWhenPersistanceLevelValidationIssuesFound() {

        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        List<ValidationIssue> mockValidationIssues = Collections.singletonList(
                mock(ValidationIssue.class)
        );

        doReturn(mockValidationIssues)
                .when(mockValidator)
                .validate(MockIdentifiableDomain);

        List<MockIdentifiableDomain> MockIdentifiableDomains = Collections.singletonList(MockIdentifiableDomain);

        crudOperationsSpy.validationCheck(MockIdentifiableDomains);
    }

    //DefaultAnnotationParam - SonarQube requires an assertion, be explicit!
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldThrowNoExceptionsIfNoValidationIssuesFoundOnAnyLevel() {

        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        doReturn(Collections.emptyList())
                .when(mockValidator)
                .validate(MockIdentifiableDomain);

        List<MockIdentifiableDomain> MockIdentifiableDomains = Collections.singletonList(MockIdentifiableDomain);

        crudOperationsSpy.validationCheck(MockIdentifiableDomains);
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
                .resourceDoesntExistCheck(MockIdentifiableDomain);

        crudOperationsSpy.resourcesDontExistCheck(MockIdentifiableDomains);

        verify(crudOperationsSpy, times(MockIdentifiableDomains.size()))
                .resourceDoesntExistCheck(MockIdentifiableDomain);
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
                .resourceDoesntExistCheck(MockIdentifiableDomain);

        crudOperationsSpy.resourcesDontExistCheck(MockIdentifiableDomains);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionIfIdIsNull() {
        MockIdentifiableDomain MockIdentifiableDomain = mock(MockIdentifiableDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(null)
                .when(MockIdentifiableDomain)
                .getId();

        crudOperationsSpy.resourceDoesntExistCheck(MockIdentifiableDomain);
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

        crudOperationsSpy.resourceDoesntExistCheck(MockIdentifiableDomain);
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

        crudOperationsSpy.resourceDoesntExistCheck(MockIdentifiableDomain);
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
                .resourceDoesntExistCheck(MockIdentifiableDomain);

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
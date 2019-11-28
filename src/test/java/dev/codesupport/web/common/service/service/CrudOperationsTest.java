package dev.codesupport.web.common.service.service;

import dev.codesupport.testutils.domain.MockDomain;
import dev.codesupport.testutils.entity.MockEntity;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.exception.ValidationException;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

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

    private static CrudOperations<MockEntity, Long, MockDomain> crudOperationsSpy;
    private static JpaRepository<MockEntity, Long> mockRepository;
    private static AbstractPersistenceValidation<MockEntity, Long, MockDomain, JpaRepository<MockEntity, Long>> mockValidator;
    private static ApplicationContext mockContext;

    @BeforeClass
    public static void init() {
        mockContext = mock(ApplicationContext.class);

        //unchecked - This is not a concern for the purposes of this test.
        //noinspection unchecked
        mockValidator = mock(AbstractPersistenceValidation.class);

        doReturn(MockEntity.class)
                .when(mockValidator)
                .getEntityType();

        Map<String, AbstractPersistenceValidation> abstractValidationMap = new HashMap<>();
        abstractValidationMap.put("mockValidator", mockValidator);

        doReturn(abstractValidationMap)
                .when(mockContext)
                .getBeansOfType(AbstractPersistenceValidation.class);

        //unchecked - This is not a concern for the purposes of this test.
        //noinspection unchecked
        mockRepository = mock(JpaRepository.class);

        CrudOperations.setContext(mockContext);

        crudOperationsSpy = spy(
                new CrudOperations<>(
                        mockRepository,
                        MockEntity.class,
                        MockDomain.class
                )
        );
    }

    @Before
    public void setup() {
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
        new CrudOperations<>(
                mockRepository,
                MockEntity.class,
                MockDomain.class
        );
    }

    @Test
    public void shouldGetFirstCorrectValidationBean() {
        doReturn(MockEntity.class)
                .when(mockValidator)
                .getEntityType();

        AbstractPersistenceValidation actual = crudOperationsSpy.getValidationBean();

        assertEquals(mockValidator, actual);
    }

    @Test
    public void shouldGetCorrectEntityById() {
        long id = 1L;
        String value = "value";

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        Optional<MockEntity> optional = Optional.of(
                new MockEntity(id, "value")
        );

        doReturn(optional)
                .when(mockRepository)
                .findById(id);

        List<MockDomain> actual = crudOperationsSpy.getById(id);

        assertEquals(returnedDomains, actual);
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

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockEntity> returnedEntities = Collections.singletonList(
                new MockEntity(id, value)
        );

        doReturn(returnedEntities)
                .when(mockRepository)
                .findAll();

        List<MockDomain> actual = crudOperationsSpy.getAll();

        assertEquals(returnedDomains, actual);
    }

    @Test
    public void shouldInvokeResourcesAlreadyExistsCheckOnCreateEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesAlreadyExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.createEntities(domainsToSave);

        verify(crudOperationsSpy, times(1))
                .resourcesAlreadyExistCheck(domainsToSave);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldBubbleServiceLayerExceptionFromResourcesAlreadyExistCheckOnCreateEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doThrow(ServiceLayerException.class)
                .when(crudOperationsSpy)
                .resourcesAlreadyExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.createEntities(domainsToSave);
    }

    @Test
    public void shouldInvokeValidationCheckOnCreateEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesAlreadyExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
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

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesAlreadyExistCheck(domainsToSave);

        doThrow(ValidationException.class)
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.createEntities(domainsToSave);
    }

    @Test
    public void shouldReturnCorrectListOfSavedDomainsOnCreateEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesAlreadyExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        List<MockDomain> actual = crudOperationsSpy.createEntities(domainsToSave);

        assertEquals(returnedDomains, actual);
    }

    @Test
    public void shouldInvokeResourcesDontExistsCheckOnUpdateEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesDontExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
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

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doThrow(ResourceNotFoundException.class)
                .when(crudOperationsSpy)
                .resourcesDontExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.updateEntities(domainsToSave);
    }

    @Test
    public void shouldInvokeValidationCheckOnUpdateEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesDontExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
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

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesDontExistCheck(domainsToSave);

        doThrow(ValidationException.class)
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        crudOperationsSpy.updateEntities(domainsToSave);
    }

    @Test
    public void shouldReturnCorrectListOfSavedDomainsOnUpdateEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourcesDontExistCheck(domainsToSave);

        doNothing()
                .when(crudOperationsSpy)
                .validationCheck(domainsToSave);

        doReturn(returnedDomains)
                .when(crudOperationsSpy)
                .saveEntities(domainsToSave);

        List<MockDomain> actual = crudOperationsSpy.updateEntities(domainsToSave);

        assertEquals(returnedDomains, actual);
    }

    @Test
    public void shouldInvokeResourcesDontExistsCheckOnDeleteEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToDelete = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockEntity> entitiesToDelete = Collections.singletonList(
                new MockEntity(id, value)
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

        List<MockDomain> domainsToSave = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockEntity> entitiesToSave = Collections.singletonList(
                new MockEntity(id, value)
        );

        List<MockDomain> returnedDomains = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockEntity> returnedEntities = Collections.singletonList(
                new MockEntity(id, value)
        );

        doReturn(returnedEntities)
                .when(mockRepository)
                .saveAll(entitiesToSave);

        List<MockDomain> actual = crudOperationsSpy.saveEntities(domainsToSave);

        assertEquals(returnedDomains, actual);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldBubbleResourceNotFoundExceptionFromResourcesDontExistCheckOnDeleteEntities() {
        long id = 1L;
        String value = "value";

        List<MockDomain> domainsToDelete = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockEntity> entitiesToDelete = Collections.singletonList(
                new MockEntity(id, value)
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

        List<MockDomain> domainsToDelete = Collections.singletonList(
                new MockDomain(id, value)
        );

        List<MockEntity> entitiesToDelete = Collections.singletonList(
                new MockEntity(id, value)
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
    public void shouldThrowValidationExceptionWhenDomainLevelValidationIssuesFound() {
        MockDomain mockDomain = mock(MockDomain.class);

        List<ValidationIssue> mockValidationIssues = Collections.singletonList(
                mock(ValidationIssue.class)
        );

        doReturn(mockValidationIssues)
                .when(mockDomain)
                .validate();

        List<MockDomain> mockDomains = Collections.singletonList(mockDomain);

        crudOperationsSpy.validationCheck(mockDomains);
    }

    @Test
    public void shouldNotInvokePersistenceLevelValidationIfDomainLevelIssuesFound() {

        MockDomain mockDomain = mock(MockDomain.class);

        List<ValidationIssue> mockValidationIssues = Collections.singletonList(
                mock(ValidationIssue.class)
        );

        doReturn(mockValidationIssues)
                .when(mockDomain)
                .validate();

        doReturn(Collections.emptyList())
                .when(mockValidator)
                .validate(mockDomain);

        List<MockDomain> mockDomains = Collections.singletonList(mockDomain);

        try {
            crudOperationsSpy.validationCheck(mockDomains);
        } catch (ValidationException e) {
            //swallowing so we can do a verify assertion
        }

        verify(mockValidator, times(0))
                .validate(mockDomain);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowValidationExceptionWhenPersistanceLevelValidationIssuesFound() {

        MockDomain mockDomain = mock(MockDomain.class);

        List<ValidationIssue> mockValidationIssues = Collections.singletonList(
                mock(ValidationIssue.class)
        );

        doReturn(Collections.emptyList())
                .when(mockDomain)
                .validate();

        doReturn(mockValidationIssues)
                .when(mockValidator)
                .validate(mockDomain);

        List<MockDomain> mockDomains = Collections.singletonList(mockDomain);

        crudOperationsSpy.validationCheck(mockDomains);
    }

    //DefaultAnnotationParam - SonarQube requires an assertion, be explicit!
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldThrowNoExceptionsIfNoValidationIssuesFoundOnAnyLevel() {

        MockDomain mockDomain = mock(MockDomain.class);

        doReturn(Collections.emptyList())
                .when(mockDomain)
                .validate();

        doReturn(Collections.emptyList())
                .when(mockValidator)
                .validate(mockDomain);

        List<MockDomain> mockDomains = Collections.singletonList(mockDomain);

        crudOperationsSpy.validationCheck(mockDomains);
    }

    @Test
    public void shouldInvokeResourceDoesntExistCheckForEachDomain() {
        MockDomain mockDomain = mock(MockDomain.class);

        List<MockDomain> mockDomains = Arrays.asList(
                mockDomain,
                mockDomain
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourceDoesntExistCheck(mockDomain);

        crudOperationsSpy.resourcesDontExistCheck(mockDomains);

        verify(crudOperationsSpy, times(mockDomains.size()))
                .resourceDoesntExistCheck(mockDomain);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldBubbleUpResourceNotFoundExceptionIfDomainDoesntExist() {
        MockDomain mockDomain = mock(MockDomain.class);

        List<MockDomain> mockDomains = Arrays.asList(
                mockDomain,
                mockDomain
        );

        //Returns issues on second check
        doNothing()
                .doThrow(ResourceNotFoundException.class)
                .when(crudOperationsSpy)
                .resourceDoesntExistCheck(mockDomain);

        crudOperationsSpy.resourcesDontExistCheck(mockDomains);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionIfIdIsNull() {
        MockDomain mockDomain = mock(MockDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(null)
                .when(mockDomain)
                .getId();

        crudOperationsSpy.resourceDoesntExistCheck(mockDomain);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionIfNotFoundInRepo() {
        long id = 1L;

        MockDomain mockDomain = mock(MockDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(mockDomain)
                .getId();

        doReturn(false)
                .when(mockRepository)
                .existsById(id);

        crudOperationsSpy.resourceDoesntExistCheck(mockDomain);
    }

    //DefaultAnnotationParam - SonarQube requires an assertion, be explicit!
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldThrowNoExceptionWhenDomainExistsForResourceDoesntExistCheck() {
        long id = 1L;

        MockDomain mockDomain = mock(MockDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(mockDomain)
                .getId();

        doReturn(true)
                .when(mockRepository)
                .existsById(id);

        crudOperationsSpy.resourceDoesntExistCheck(mockDomain);
    }


    @Test
    public void shouldInvokeResourceAlreadyExistsCheckForEachDomain() {
        MockDomain mockDomain = mock(MockDomain.class);

        List<MockDomain> mockDomains = Arrays.asList(
                mockDomain,
                mockDomain
        );

        doNothing()
                .when(crudOperationsSpy)
                .resourceDoesntExistCheck(mockDomain);

        crudOperationsSpy.resourcesAlreadyExistCheck(mockDomains);

        verify(crudOperationsSpy, times(mockDomains.size()))
                .resourceAlreadyExistsCheck(mockDomain);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldBubbleUpServiceLayerExceptionIfDomainAlreadyExists() {
        MockDomain mockDomain = mock(MockDomain.class);

        List<MockDomain> mockDomains = Arrays.asList(
                mockDomain,
                mockDomain
        );

        //Returns issues on second check
        doNothing()
                .doThrow(ServiceLayerException.class)
                .when(crudOperationsSpy)
                .resourceAlreadyExistsCheck(mockDomain);

        crudOperationsSpy.resourcesAlreadyExistCheck(mockDomains);
    }

    @Test
    public void shouldNotInvokeRepoCheckIfIdIsNullForResourceAlreadyExistsCheck() {
        long id = 1L;

        MockDomain mockDomain = mock(MockDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(null)
                .when(mockDomain)
                .getId();

        crudOperationsSpy.resourceAlreadyExistsCheck(mockDomain);

        verify(mockRepository, times(0))
                .existsById(id);
    }

    @Test(expected = ServiceLayerException.class)
    public void shouldThrowServiceLayerExceptionIfIdIsNotNullAndIsFoundInRepo() {
        long id = 1L;

        MockDomain mockDomain = mock(MockDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(mockDomain)
                .getId();

        doReturn(true)
                .when(mockRepository)
                .existsById(id);

        crudOperationsSpy.resourceAlreadyExistsCheck(mockDomain);
    }

    //DefaultAnnotationParam - SonarQube requires an assertion, be explicit!
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldThrowNoExceptionWhenDomainDoesntExistForResourceAlreadyExistsCheck() {
        long id = 1L;

        MockDomain mockDomain = mock(MockDomain.class);

        //ResultOfMethodCallIgnored - Not doing method call, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(id)
                .when(mockDomain)
                .getId();

        doReturn(false)
                .when(mockRepository)
                .existsById(id);

        crudOperationsSpy.resourceAlreadyExistsCheck(mockDomain);
    }
}
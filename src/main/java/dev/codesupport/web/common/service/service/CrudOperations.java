package dev.codesupport.web.common.service.service;

import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.exception.ValidationException;
import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidation;
import dev.codesupport.web.common.util.ListUtils;
import lombok.Setter;
import dev.codesupport.web.common.domain.AbstractValidatable;
import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.util.MappingUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A class for performing generic CRUD operations of a particular resource
 *
 * @param <E> Entity class associated to the respected resource. Must implement IdentifiableEntity interface.
 * @param <I> Associated type of the Id property on the given resource.
 * @param <D> Domain class associated to the respected resource. Must implement Validatable interface.
 * @see IdentifiableEntity
 * @see AbstractValidatable
 */
public class CrudOperations<E extends IdentifiableEntity<I>, I, D extends AbstractValidatable<I>> {

    private final JpaRepository<E, I> jpaRepository;
    private final Class<E> entityClass;
    private final Class<D> domainClass;
    private final AbstractPersistenceValidation<E, I, D, ? extends JpaRepository<E, I>> validation;
    @Setter
    private static ApplicationContext context;

    /**
     * Creates a new crudOperations object for a given resource.
     *
     * @param jpaRepository Reference to the jpaRepository associated with the resource.
     * @param entityClass   Reference to the entity class associated with the resource.
     * @param domainClass   Reference to the domain class associated with the resource.
     * @throws ConfigurationException when the ApplicationContext has not been configured.
     */
    public CrudOperations(
            JpaRepository<E, I> jpaRepository,
            Class<E> entityClass,
            Class<D> domainClass
    ) {
        this.jpaRepository = jpaRepository;
        this.entityClass = entityClass;
        this.domainClass = domainClass;

        validation = getValidationBean();
    }

    /**
     * Locates the desired validation component for the given resource if one exists.
     *
     * @return the validation component associated to the resource
     * @throws ConfigurationException when the ApplicationContext has not been configured.
     */
    AbstractPersistenceValidation<E, I, D, JpaRepository<E, I>> getValidationBean() {
        if (context == null) {
            throw new ConfigurationException("CrudOperations ApplicationContext not configured");
        }

        AbstractPersistenceValidation<E, I, D, JpaRepository<E, I>> validationToReturn = null;

        Map<String, AbstractPersistenceValidation> beans = context.getBeansOfType(AbstractPersistenceValidation.class);

        for (Map.Entry<String, AbstractPersistenceValidation> bean : beans.entrySet()) {
            AbstractPersistenceValidation validationBean = bean.getValue();
            if (
                    validationToReturn == null &&
                            validationBean.getEntityType().equals(entityClass)
            ) {
                //This cast should be fine, but would be nice to write it in a better way
                //noinspection unchecked
                validationToReturn = validationBean;
            }
        }
        return validationToReturn;
    }

    /**
     * Gets a list of the resource data matching the given id
     *
     * @param id The id of the desired resource data
     * @return The resource data list for the given id
     */
    public List<D> getById(I id) {
        Optional<E> object = jpaRepository.findById(id);

        if (!object.isPresent()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }

        return Collections.singletonList(MappingUtils.convertToType(object.get(), domainClass));
    }

    /**
     * Gets a list of all data for a given resource
     *
     * @return List of data for the given resource
     */
    public List<D> getAll() {
        return MappingUtils.convertToType(jpaRepository.findAll(), domainClass);
    }

    /**
     * Attempts to persist new resource data
     * <p>In creating data, first it is validated that the data does not already exist, (In the case of specified IDs)
     * and that the provided data is valid.</p>
     *
     * @param domainObject The resource data to persist
     * @return List of the persisted data including fields added at the time of persistence
     * @throws ServiceLayerException if the data already exists.
     */
    public List<D> createEntity(D domainObject) {
        return createEntities(Collections.singletonList(domainObject));
    }

    /**
     * Attempts to persist new resource data
     * <p>In creating data, first it is validated that the data does not already exist, (In the case of specified IDs)
     * and that the provided data is valid.</p>
     *
     * @param domainObjects The list of resource data to persist
     * @return List of the persisted data including fields added at the time of persistence
     * @throws ServiceLayerException if the data already exists.
     */
    public List<D> createEntities(List<D> domainObjects) {
        emptyArgumentListCheck(domainObjects);
        domainObjects.forEach(object -> object.setId(null));
        validationCheck(domainObjects);

        return saveEntities(domainObjects);
    }

    /**
     * Attempts to persist updated resource data
     * <p>In updating the data, first it is validated that the data to update already exists, and that the provided
     * data is valid.</p>
     *
     * @param domainObjects The list of resource data to update
     * @return List of the updated data including fields added at the time of persistence
     * @throws ResourceNotFoundException if the data does not exist.
     */
    public List<D> updateEntities(List<D> domainObjects) {
        emptyArgumentListCheck(domainObjects);
        resourcesDontExistCheck(domainObjects);
        validationCheck(domainObjects);

        return saveEntities(domainObjects);
    }

    /**
     * Saves the provided data to the persistent storage.
     *
     * @param domainObjects The list of resource data to persist
     * @return List of the persisted data including fields added at the time of persistence
     */
    List<D> saveEntities(List<D> domainObjects) {
        List<E> entities = MappingUtils.convertToType(domainObjects, entityClass);

        List<E> savedEntities = jpaRepository.saveAll(entities);

        return MappingUtils.convertToType(savedEntities, domainClass);
    }

    /**
     * Attempts to delete the given data from the persistent storage.
     * <p>In deleting the data, first it is validated that the data to delete already exists.</p>
     *
     * @param domainObjects The list of resource data to delete
     * @throws ResourceNotFoundException if the data does not exist.
     */
    public void deleteEntities(List<D> domainObjects) {
        emptyArgumentListCheck(domainObjects);
        resourcesDontExistCheck(domainObjects);

        List<E> entities = MappingUtils.convertToType(domainObjects, entityClass);

        jpaRepository.deleteAll(entities);
    }

    /**
     * Validates the given resource data list.
     * <p>First validates at the domain level, throwing an exception if any {@see ValidationIssue} found.  If not, persistence level
     * validations are executed, again throwing an exception if any {@see ValidationIssue} are found.</p>
     *
     * @param domainObjects The resource data list to validate
     * @throws ValidationException if any {@see ValidationIssue}s are found
     * @link ValidationIssue
     */
    void validationCheck(List<D> domainObjects) {
        List<ValidationIssue> validationIssues = new ArrayList<>();
        for (D domainObject : domainObjects) {
            validationIssues.addAll(domainObject.validate());
        }
        if (validationIssues.isEmpty() && validation != null) {
            for (D domainObject : domainObjects) {
                validationIssues.addAll(validation.validate(domainObject));
            }
        }
        if (!validationIssues.isEmpty()) {
            throw new ValidationException(validationIssues);
        }
    }

    /**
     * Checks if any resource data in the provided list doesn't exist.
     *
     * @param domainObjects The resource data list to check.
     * @throws ResourceNotFoundException if any resource data doesn't exist.
     */
    void resourcesDontExistCheck(List<D> domainObjects) {
        for (D domainObject : domainObjects) {
            resourceDoesntExistCheck(domainObject);
        }
    }

    /**
     * Checks if the given resource data doesn't exists.
     *
     * @param domainObject The resource data to check
     * @throws ResourceNotFoundException if the resource data doesn't exist.
     */
    void resourceDoesntExistCheck(D domainObject) {
        if (domainObject.getId() == null || !jpaRepository.existsById(domainObject.getId())) {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }
    }

    /**
     * Checks if any resource data in the provided list already exists.
     *
     * @param domainObjects The resource data list to check.
     * @throws ServiceLayerException if any resource data already exists.
     */
    void resourcesAlreadyExistCheck(List<D> domainObjects) {
        for (D domainObject : domainObjects) {
            resourceAlreadyExistsCheck(domainObject);
        }
    }

    /**
     * Checks if the given resource data already exists.
     *
     * @param domainObject The resource data to check
     * @throws ServiceLayerException if the resource data already exists.
     */
    void resourceAlreadyExistsCheck(D domainObject) {
        if (domainObject.getId() != null && jpaRepository.existsById(domainObject.getId())) {
            throw new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS);
        }
    }

    void emptyArgumentListCheck(List<D> domainObjects) {
        if (ListUtils.isEmpty(domainObjects)) {
            throw new ServiceLayerException(ServiceLayerException.Reason.EMPTY_PAYLOAD);
        }
    }
}

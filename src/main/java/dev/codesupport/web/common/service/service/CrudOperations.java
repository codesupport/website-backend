package dev.codesupport.web.common.service.service;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.exception.ValidationException;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidator;
import dev.codesupport.web.common.util.MappingUtils;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A class for performing generic CRUD operations of a particular resource
 *
 * @param <E> Entity class associated to the respected resource. Must implement IdentifiableEntity interface.
 * @param <I> Associated type of the Id property on the given resource.
 * @param <D> Domain class associated to the respected resource. Must implement Validatable interface.
 * @see IdentifiableEntity
 * @see IdentifiableDomain
 */
public class CrudOperations<E extends IdentifiableEntity<I>, D extends IdentifiableDomain<I>, I> {

    protected final JpaRepository<E, I> jpaRepository;
    protected final Class<E> entityClass;
    protected final Class<D> domainClass;
    private AbstractPersistenceValidator<E, I, D, ? extends JpaRepository<E, I>> validation;
    @Setter
    private static ApplicationContext context;
    @Setter
    private Consumer<E> preSaveEntities;
    @Setter
    private Consumer<E> preGetEntities;

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
    }

    /**
     * Locates the desired validation component for the given resource if one exists.
     *
     * @throws ConfigurationException when the ApplicationContext has not been configured.
     */
    @PostConstruct
    @VisibleForTesting
    void setupValidationBean() {
        if (context == null) {
            throw new ConfigurationException("CrudOperations ApplicationContext not configured");
        }

        AbstractPersistenceValidator<E, I, D, JpaRepository<E, I>> validationToReturn = null;

        //rawtypes - This is fine for this logic.
        //noinspection rawtypes
        Map<String, AbstractPersistenceValidator> beans = context.getBeansOfType(AbstractPersistenceValidator.class);

        //rawtypes - This is fine for this logic.
        //noinspection rawtypes
        for (Map.Entry<String, AbstractPersistenceValidator> bean : beans.entrySet()) {
            //rawtypes - This is fine for this logic.
            //noinspection rawtypes
            AbstractPersistenceValidator validationBean = bean.getValue();
            if (
                    validationToReturn == null &&
                            validationBean.getEntityType().equals(entityClass)
            ) {
                //This cast should be fine, but would be nice to write it in a better way
                //noinspection unchecked
                validationToReturn = validationBean;
            }
        }
        validation = validationToReturn;
    }

    @VisibleForTesting
    void preGetEntities(List<E> entities) {
        if (preGetEntities != null) {
            entities.forEach(preGetEntities);
        }
    }

    /**
     * Gets a list of the resource data matching the given id
     *
     * @param id The id of the desired resource data
     * @return The resource data list for the given id
     */
    public D getById(I id) {
        Optional<E> optional = jpaRepository.findById(id);

        if (optional.isPresent()) {
            E entity = optional.get();
            preGetEntities(Collections.singletonList(entity));
            return MappingUtils.convertToType(entity, domainClass);
        }

        throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
    }

    /**
     * Gets a list of all data for a given resource
     *
     * @return List of data for the given resource
     */
    public List<D> getAll() {
        List<E> entities = jpaRepository.findAll();
        preGetEntities(entities);
        return MappingUtils.convertToType(entities, domainClass);
    }

    /**
     * Attempts to persist new resource data
     * <p>In creating data, first it is validated that the data does not already exist, (In the case of specified IDs)
     * and that the provided data is valid.</p>
     *
     * @param domainObject The resource data to persist
     * @return The persisted data including fields added at the time of persistence
     * @throws ServiceLayerException if the data already exists.
     */
    public D createEntity(D domainObject) {
        return createEntities(Collections.singletonList(domainObject)).get(0);
    }

    protected void preCreate(List<D> domainObjects) {
        domainObjects.forEach(object -> object.setId(null));
        validationCheck(domainObjects);
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
        preCreate(domainObjects);

        return saveEntities(domainObjects);
    }

    /**
     * Attempts to persist updated resource data
     * <p>In updating the data, first it is validated that the data to update already exists, and that the provided
     * data is valid.</p>
     *
     * @param domainObject The resource data to update
     * @return The updated data including fields added at the time of persistence
     * @throws ResourceNotFoundException if the data does not exist.
     */
    public D updateEntity(D domainObject) {
        return updateEntities(Collections.singletonList(domainObject)).get(0);
    }

    protected void preUpdate(List<D> domainObjects) {
        resourcesDontExistCheck(domainObjects);
        validationCheck(domainObjects);
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
        preUpdate(domainObjects);

        return saveEntities(domainObjects);
    }

    /**
     * Allows the ability to perform logic on the entities prior to persisting them.
     *
     * @param entityObjects The entities pending persistence to the db
     */
    protected void preSaveEntities(List<E> entityObjects) {
        if (preSaveEntities != null) {
            entityObjects.forEach(preSaveEntities);
        }
    }

    /**
     * Saves the provided data to the persistent storage.
     *
     * @param domainObjects The list of resource data to persist
     * @return List of the persisted data including fields added at the time of persistence
     */
    @VisibleForTesting
    List<D> saveEntities(List<D> domainObjects) {
        List<E> entities = MappingUtils.convertToType(domainObjects, entityClass);

        preSaveEntities(entities);

        List<E> savedEntities = jpaRepository.saveAll(entities);

        return MappingUtils.convertToType(savedEntities, domainClass);
    }

    /**
     * Attempts to delete the given data from the persistent storage.
     * <p>In deleting the data, first it is validated that the data to delete already exists.</p>
     *
     * @param domainObject The resource data to delete
     * @throws ResourceNotFoundException if the data does not exist.
     */
    public int deleteEntity(D domainObject) {
        return deleteEntities(Collections.singletonList(domainObject));
    }

    protected void preDelete(List<D> domainObjects) {
        resourcesDontExistCheck(domainObjects);
    }

    /**
     * Attempts to delete the given data from the persistent storage.
     * <p>In deleting the data, first it is validated that the data to delete already exists.</p>
     *
     * @param domainObjects The list of resource data to delete
     * @throws ResourceNotFoundException if the data does not exist.
     */
    public int deleteEntities(List<D> domainObjects) {
        preDelete(domainObjects);

        List<E> entities = MappingUtils.convertToType(domainObjects, entityClass);

        jpaRepository.deleteAll(entities);

        return domainObjects.size();
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
    @VisibleForTesting
    void validationCheck(List<D> domainObjects) {
        List<ValidationIssue> validationIssues = new ArrayList<>();

        if (validation != null) {
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
    @VisibleForTesting
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
    @VisibleForTesting
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
    @VisibleForTesting
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
    @VisibleForTesting
    void resourceAlreadyExistsCheck(D domainObject) {
        if (domainObject.getId() != null && jpaRepository.existsById(domainObject.getId())) {
            throw new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS);
        }
    }
}

package dev.codesupport.web.common.service.service;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.util.MappingUtils;
import lombok.Setter;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private final List<PrePersistCheck<D, I>> preCreateChecks;
    private final List<PrePersistCheck<D, I>> preUpdateChecks;
    private final List<PrePersistCheck<D, I>> preDeleteChecks;
    protected final CrudRepository<E, I> crudRepository;
    protected final Class<E> entityClass;
    protected final Class<D> domainClass;

    @Setter
    private static ApplicationContext context;
    @Setter
    private Consumer<E> preSaveEntities;
    @Setter
    private Consumer<E> preGetEntities;

    /**
     * Creates a new crudOperations object for a given resource.
     *
     * @param crudRepository Reference to the jpaRepository associated with the resource.
     * @param entityClass   Reference to the entity class associated with the resource.
     * @param domainClass   Reference to the domain class associated with the resource.
     * @throws ConfigurationException when the ApplicationContext has not been configured.
     */
    public CrudOperations(
            CrudRepository<E, I> crudRepository,
            Class<E> entityClass,
            Class<D> domainClass
    ) {
        this.crudRepository = crudRepository;
        this.entityClass = entityClass;
        this.domainClass = domainClass;
        this.preCreateChecks = new ArrayList<>();
        this.preUpdateChecks = new ArrayList<>();
        this.preDeleteChecks = new ArrayList<>();
        setUpPrePersistChecks();
    }

    /**
     * Configures pre-persist checks
     */
    @VisibleForTesting
    void setUpPrePersistChecks() {
        addPreCreateCheck((domains -> domains.forEach(o -> o.setId(null))));

        addPreUpdateCheck(this::resourcesDoNotExistCheck);

        addPreDeleteCheck(this::resourcesDoNotExistCheck);
    }

    protected void addPreCreateCheck(PrePersistCheck<D, I> check) {
        preCreateChecks.add(check);
    }

    protected void addPreUpdateCheck(PrePersistCheck<D, I> check) {
        preUpdateChecks.add(check);
    }

    protected void addPreDeleteCheck(PrePersistCheck<D, I> check) {
        preDeleteChecks.add(check);
    }

    @VisibleForTesting
    void runPrePersistChecks(Collection<PrePersistCheck<D, I>> checks, List<D> objects) {
        for (PrePersistCheck<D, I> check : checks) {
            check.check(objects);
        }
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
        E entity = crudRepository.getById(id);
        preGetEntities(Collections.singletonList(entity));
        return MappingUtils.convertToType(entity, domainClass);
    }

    /**
     * Gets a list of all data for a given resource
     *
     * @return List of data for the given resource
     */
    public List<D> getAll() {
        List<E> entities = crudRepository.findAll();
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

    @VisibleForTesting
    void preCreate(List<D> domainObjects) {
        runPrePersistChecks(preCreateChecks, domainObjects);
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

    @VisibleForTesting
    void preUpdate(List<D> domainObjects) {
        runPrePersistChecks(preUpdateChecks, domainObjects);
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

        List<E> savedEntities = crudRepository.saveAll(entities);

        savedEntities = savedEntities.stream().map(E::getId).map(crudRepository::getById).collect(Collectors.toList());

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

    @VisibleForTesting
    void preDelete(List<D> domainObjects) {
        runPrePersistChecks(preDeleteChecks, domainObjects);
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

        crudRepository.deleteAll(entities);

        return domainObjects.size();
    }

    /**
     * Checks if any resource data in the provided list doesn't exist.
     *
     * @param domainObjects The resource data list to check.
     * @throws ResourceNotFoundException if any resource data doesn't exist.
     */
    @VisibleForTesting
    void resourcesDoNotExistCheck(Collection<D> domainObjects) {
        for (D domainObject : domainObjects) {
            resourceDoesNotExistCheck(domainObject);
        }
    }

    /**
     * Checks if the given resource data doesn't exists.
     *
     * @param domainObject The resource data to check
     * @throws ResourceNotFoundException if the resource data doesn't exist.
     */
    @VisibleForTesting
    void resourceDoesNotExistCheck(D domainObject) {
        if (domainObject.getId() == null || !crudRepository.existsById(domainObject.getId())) {
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
        if (domainObject.getId() != null && crudRepository.existsById(domainObject.getId())) {
            throw new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS);
        }
    }

    @FunctionalInterface
    protected interface PrePersistCheck<D extends IdentifiableDomain<I>, I> {

        void check(Collection<D> domain);

    }

}

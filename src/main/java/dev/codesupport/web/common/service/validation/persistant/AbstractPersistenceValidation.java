package dev.codesupport.web.common.service.validation.persistant;

import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.domain.IdentifiableDomain;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Abstract class for building PersistenceValidation classes.
 *
 * <p>PersistenceValidation classes are used for validating resources against persistent data, such as checking
 * on unique values, or combination of values within a table.</p>
 *
 * @param <E> Entity class associated to the respected resource. Must implement IdentifiableEntity interface.
 * @param <I> Associated type of the Id property on the given resource.
 * @param <D> Domain class associated to the respected resource. Must implement IdentifiableDomain interface.
 * @param <C> CrudRepository associated to the respected resource. Must extend CrudRepository interface.
 * @see IdentifiableEntity
 * @see IdentifiableDomain
 */
public abstract class AbstractPersistenceValidation<E extends IdentifiableEntity<I>, I, D extends IdentifiableDomain<I>, C extends CrudRepository<E, I>> {

    /**
     * The associated repository for the given resource
     */
    protected C repository;

    public AbstractPersistenceValidation(C repository) {
        this.repository = repository;
    }

    /**
     * Validates a given object against persistent data.
     *
     * @param domainObject The object to be validated against
     * @return a list of ValidationIssues for the given object, returns an empty list if no validation issues found.
     * @see ValidationIssue
     */
    public abstract List<ValidationIssue> validate(D domainObject);

    /**
     * Returns the Entity type associated with the validation.
     * <p>Used when looking for a validation component in the {@link dev.codesupport.web.common.service.service.CrudOperations}
     * class for the correct Entity type.</p>
     *
     * @return The associated Entity for the Validation subclass.
     */
    public abstract Class<E> getEntityType();

}

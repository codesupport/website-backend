package dev.codesupport.web.common.service.validation.persistant;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.ParameterizedType;
import java.security.InvalidParameterException;
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
@Data
public abstract class AbstractPersistenceValidator<E extends IdentifiableEntity<I>, I, D extends IdentifiableDomain<I>, C extends CrudRepository<E, I>> {

    private final Class<E> entityType;

    /**
     * The associated repository for the given resource
     */
    protected C repository;

    public AbstractPersistenceValidator(C repository) {
        this.repository = repository;

        Object type = this.getClass().getGenericSuperclass();

        // Gets the Entity type defined by the child class.
        if (type instanceof ParameterizedType) {
            Object clazz = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (clazz instanceof Class) {
                //unchecked - We can be sure it's the right one.
                //noinspection unchecked
                entityType = (Class<E>) clazz;
            } else {
                throw new InvalidParameterException("Invalid class parameter type for generic.");
            }
        } else {
            throw new InvalidParameterException("Invalid class parameter type for generic.");
        }
    }

    /**
     * Validates a given object against persistent data.
     *
     * @param domainObject The object to be validated against
     * @return a list of ValidationIssues for the given object, returns an empty list if no validation issues found.
     * @see ValidationIssue
     */
    public abstract List<ValidationIssue> validate(D domainObject);

}

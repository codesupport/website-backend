package dev.codesupport.web.common.service.service;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.data.domain.AuditableDomain;
import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import dev.codesupport.web.common.data.entity.AuditableEntity;
import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.StaleDataException;

import java.util.Collection;

/**
 * A class for performing generic CRUD operations of a particular resource
 *
 * @param <E> Entity class associated to the respected resource. Must implement IdentifiableEntity interface.
 * @param <I> Associated type of the Id property on the given resource.
 * @param <D> Domain class associated to the respected resource. Must implement Validatable interface.
 * @see IdentifiableEntity
 * @see IdentifiableDomain
 */
public class CrudAuditableOperations<T, E extends AuditableEntity<I, T>, D extends AuditableDomain<I, T>, I>
        extends CrudOperations<E, D, I> {

    /**
     * Creates a new crudAuditableOperations object for a given resource.
     *
     * @param crudRepository Reference to the jpaRepository associated with the resource.
     * @param entityClass   Reference to the entity class associated with the resource.
     * @param domainClass   Reference to the domain class associated with the resource.
     * @throws ConfigurationException when the ApplicationContext has not been configured.
     */
    public CrudAuditableOperations(
            CrudRepository<E, I> crudRepository,
            Class<E> entityClass,
            Class<D> domainClass
    ) {
        super(crudRepository, entityClass, domainClass);
        setUpStaleDataChecks();
    }

    @VisibleForTesting
    void setUpStaleDataChecks() {
        addPreUpdateCheck(this::staleDataChecks);
        addPreDeleteCheck(this::staleDataChecks);
    }

    @VisibleForTesting
    void staleDataChecks(Collection<D> objects) {
        objects.forEach(this::staleDataCheck);
    }

    @VisibleForTesting
    void staleDataCheck(D object) {
        AuditableEntity<I, T> entity = crudRepository.getById(object.getId());

        if (!entity.getUpdatedOn().equals(object.getUpdatedOn())) {
            throw new StaleDataException();
        }
    }

}

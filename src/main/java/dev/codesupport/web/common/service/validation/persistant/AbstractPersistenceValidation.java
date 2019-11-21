package dev.codesupport.web.common.service.validation.persistant;

import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.domain.IdentifiableDomain;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public abstract class AbstractPersistenceValidation<E extends IdentifiableEntity<I>, I, D extends IdentifiableDomain<I>, C extends CrudRepository<E, I>> {

    protected C repository;

    public AbstractPersistenceValidation(C repository) {
        this.repository = repository;
    }

    public abstract List<ValidationIssue> validate(D domainObject);

    public abstract Class<E> getEntityType();

}

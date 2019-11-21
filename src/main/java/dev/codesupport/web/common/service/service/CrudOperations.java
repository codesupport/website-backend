package dev.codesupport.web.common.service.service;

import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.exception.ValidationException;
import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidation;
import lombok.Setter;
import dev.codesupport.web.common.domain.Validatable;
import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.util.MappingUtils;
import org.assertj.core.util.Lists;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CrudOperations<E extends IdentifiableEntity<I>, I, D extends Validatable<I>> {

    private final CrudRepository<E, I> crudRepository;
    private final Class<E> entityClass;
    private final Class<D> domainClass;
    private final AbstractPersistenceValidation<E, I, D, ? extends CrudRepository<E, I>> validation;
    @Setter
    private static ApplicationContext context;

    public CrudOperations(
            CrudRepository<E, I> crudRepository,
            Class<E> entityClass,
            Class<D> domainClass
    ) {
        this.crudRepository = crudRepository;
        this.entityClass = entityClass;
        this.domainClass = domainClass;

        validation = getValidationBean();
    }

    AbstractPersistenceValidation<E, I, D, CrudRepository<E, I>> getValidationBean() {
        if (context == null) {
            throw new ConfigurationException("CrudOperations ApplicationContext not configured");
        }

        AbstractPersistenceValidation<E, I, D, CrudRepository<E, I>> validationToReturn = null;

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

    public List<D> getById(I id) {
        Optional<E> object = crudRepository.findById(id);

        if (!object.isPresent()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }

        return Collections.singletonList(MappingUtils.convertToType(object.get(), domainClass));
    }

    public List<D> getAll() {
        return MappingUtils.convertToType(Lists.newArrayList(crudRepository.findAll()), domainClass);
    }

    public List<D> createEntities(List<D> domainObjects) {
        resourcesAlreadyExistCheck(domainObjects);
        validationCheck(domainObjects);

        return saveEntities(domainObjects);
    }

    public List<D> updateEntities(List<D> domainObjects) {
        resourcesDontExistCheck(domainObjects);
        validationCheck(domainObjects);

        return saveEntities(domainObjects);
    }

    List<D> saveEntities(List<D> domainObjects) {
        List<E> entities = MappingUtils.convertToType(domainObjects, entityClass);

        List<E> savedEntities = Lists.newArrayList(crudRepository.saveAll(entities));

        return MappingUtils.convertToType(savedEntities, domainClass);
    }

    public void deleteEntities(List<D> domainObjects) {
        resourcesDontExistCheck(domainObjects);

        List<E> entities = MappingUtils.convertToType(domainObjects, entityClass);

        crudRepository.deleteAll(entities);
    }

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

    void resourcesDontExistCheck(List<D> domainObjects) {
        for (D domainObject : domainObjects) {
            resourceDoesntExistCheck(domainObject);
        }
    }

    void resourceDoesntExistCheck(D domainObject) {
        if (domainObject.getId() == null || !crudRepository.existsById(domainObject.getId())) {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }
    }

    void resourcesAlreadyExistCheck(List<D> domainObjects) {
        for (D domainObject : domainObjects) {
            resourceAlreadyExistsCheck(domainObject);
        }
    }

    void resourceAlreadyExistsCheck(D domainObject) {
        if (domainObject.getId() != null && crudRepository.existsById(domainObject.getId())) {
            throw new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS);
        }
    }

}

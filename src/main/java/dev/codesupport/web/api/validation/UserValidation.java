package dev.codesupport.web.api.validation;

import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidation;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.entity.UserEntity_;
import dev.codesupport.web.api.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidation extends AbstractPersistenceValidation<UserEntity, Long, User, UserRepository> {

    @Autowired
    UserValidation(UserRepository repository) {
        super(repository);
    }

    @Override
    public List<ValidationIssue> validate(User domainObject) {
        List<ValidationIssue> validationIssues = new ArrayList<>();

        if (repository.existsByUsername(domainObject.getUsername())) {
            validationIssues.add(domainObject.createDuplicateParameter(null, UserEntity_.USERNAME));
        }

        return validationIssues;
    }

    @Override
    public Class<UserEntity> getEntityType() {
        return UserEntity.class;
    }

}
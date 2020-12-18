package dev.codesupport.web.api.validation;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.entity.UserEntity_;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidator;
import dev.codesupport.web.domain.NewUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Persistence level validation for User resources.
 * <p>Validates that no other user exists within the persistent storage with the same username or email.</p>
 *
 * @see AbstractPersistenceValidator
 */
@Component
public class NewUserValidator extends AbstractPersistenceValidator<UserEntity, Long, NewUser, UserRepository> {

    @Autowired
    NewUserValidator(UserRepository repository) {
        super(repository);
    }

    @Override
    public Set<ValidationIssue> validate(NewUser domainObject) {
        Set<ValidationIssue> validationIssues = new HashSet<>();

        if (repository.existsByAliasIgnoreCase(domainObject.getAlias())) {
            validationIssues.add(ValidationIssue.duplicateParameter(null, UserEntity_.ALIAS));
        }

        if (repository.existsByEmailIgnoreCase(domainObject.getEmail())) {
            validationIssues.add(ValidationIssue.duplicateParameter(null, UserEntity_.EMAIL));
        }

        return validationIssues;
    }

}
package dev.codesupport.web.api.validation;

import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidator;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.entity.UserEntity_;
import dev.codesupport.web.api.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Persistence level validation for User resources.
 * <p>Validates that no other {@link User} resource exists within the persistent storage with the same username.</p>
 *
 * @see AbstractPersistenceValidator
 */
@Component
public class UserValidator extends AbstractPersistenceValidator<UserEntity, Long, User, UserRepository> {

    @Autowired
    UserValidator(UserRepository repository) {
        super(repository);
    }

    @Override
    public List<ValidationIssue> validate(User domainObject) {
        List<ValidationIssue> validationIssues = new ArrayList<>();

        if (repository.existsByAliasIgnoreCase(domainObject.getAlias())) {
            validationIssues.add(ValidationIssue.duplicateParameter(null, UserEntity_.ALIAS));
        }

        if (repository.existsByEmailIgnoreCase(domainObject.getEmail())) {
            validationIssues.add(ValidationIssue.duplicateParameter(null, UserEntity_.EMAIL));
        }

        //TODO: Check if avatar image exists

        return validationIssues;
    }

}
package dev.codesupport.web.api.service;

import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Handles the business logic for the various resource operations provided by the API contract endpoints.
 */
@Service
public class UserService {

    private final CrudOperations<UserEntity, Long, User> userCrudOperations;

    @Autowired
    UserService(
            UserRepository userRepository
    ) {
        userCrudOperations = new CrudOperations<>(userRepository, UserEntity.class, User.class);
    }

    public List<User> getUserById(Long id) {
        return userCrudOperations.getById(id);
    }

    public List<User> findAllUsers() {
        return userCrudOperations.getAll();
    }

    public List<User> createUsers(List<User> users) {
        return userCrudOperations.createEntities(users);
    }

    public List<User> updateUsers(List<User> users) {
        return userCrudOperations.updateEntities(users);
    }

    public List<User> deleteUsers(List<User> users) {
        userCrudOperations.deleteEntities(users);
        //TODO: What should this return?
        return Collections.emptyList();
    }
}
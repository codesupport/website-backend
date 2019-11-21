package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
public class UserControllerImpl implements UserController {

    private final UserService service;

    @Autowired
    public UserControllerImpl(UserService userService) {
        service = userService;
    }

    @Override
    public RestResponse<User> getAllUsers() {
        return RestResponse.restResponse(service.findAllUsers());
    }

    @Override
    public RestResponse<User> getUserById(Long id) {
        return RestResponse.restResponse(service.getUserById(id));
    }

    @Override
    public RestResponse<User> createUsers(List<User> users) {
        return RestResponse.restResponse(service.createUsers(users));
    }

    @Override
    public RestResponse<User> updateUsers(List<User> users) {
        return RestResponse.restResponse(service.updateUsers(users));
    }

    @Override
    public RestResponse<User> deleteUsers(List<User> users) {
        return RestResponse.restResponse(service.deleteUsers(users));
    }
}

package dev.codesupport.testutils.controller;

import dev.codesupport.web.api.controller.UserController;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//unused - This is found and instantiated dynamically by spring
@SuppressWarnings("unused")
@Component
@RequestMapping("testmapping/")
public class MockUserController implements UserController {

    @Override
    public RestResponse<User> getAllUsers() {
        return null;
    }

    @Override
    public RestResponse<User> getUserById(Long id) {
        return null;
    }

    @Override
    public RestResponse<User> createUsers(List<User> users) {
        return null;
    }

    @Override
    public RestResponse<User> updateUsers(List<User> users) {
        return null;
    }

    @Override
    public RestResponse<User> deleteUsers(List<User> users) {
        return null;
    }

}

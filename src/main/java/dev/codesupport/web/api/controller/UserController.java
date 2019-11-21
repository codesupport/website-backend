package dev.codesupport.web.api.controller;

import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.domain.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public interface UserController {

    @GetMapping("/users")
    RestResponse<User> getAllUsers();

    @GetMapping("/users/{id}")
    RestResponse<User> getUserById(@PathVariable Long id);

    @PostMapping("/users")
    RestResponse<User> createUsers(@RequestBody List<User> users);

    @PutMapping("/users")
    RestResponse<User> updateUsers(@RequestBody List<User> users);

    @DeleteMapping("/users")
    RestResponse<User> deleteUsers(@RequestBody List<User> users);
}

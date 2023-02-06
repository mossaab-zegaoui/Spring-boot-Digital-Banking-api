package com.example.digitalbanking.web;

import com.example.digitalbanking.entities.UserEntity;
import com.example.digitalbanking.services.AuthenticationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public List<UserEntity> getUsers() {
        return authenticationService.getUsers();
    }

    @GetMapping("/{userName}")
    private UserEntity getUser(@PathVariable String userName) {
        return authenticationService.loadUserByName(userName);
    }

    @PostMapping
    public UserEntity saveUser(@RequestBody UserEntity user) {
        return authenticationService.saveUser(user);
    }

    @PutMapping("/{userId}")
    private UserEntity updateUser(@PathVariable Long userId,
                                  @RequestBody UserEntity user) {
        user.setId(userId);
        return authenticationService.saveUser(user);
    }
}

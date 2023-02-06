package com.example.digitalbanking.services;

import com.example.digitalbanking.entities.Role;
import com.example.digitalbanking.entities.UserEntity;

import java.util.List;

public interface AuthenticationService {
    UserEntity loadUserByName(String userName);

    Role saveRole(Role role);

    UserEntity saveUser(UserEntity user);

    void assignRoleToUSer(String roleName, String userName);

    List<UserEntity> getUsers();
}

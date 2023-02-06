package com.example.digitalbanking.services;

import com.example.digitalbanking.entities.Role;
import com.example.digitalbanking.entities.UserEntity;
import com.example.digitalbanking.repositories.RoleRepository;
import com.example.digitalbanking.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(RoleRepository roleRepository,
                                     UserRepository userRepository,
                                     PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity loadUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void assignRoleToUSer(String roleName, String userName) {
        Role role = roleRepository.findByName(roleName);
        UserEntity user = userRepository.findByUserName(userName);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }
}

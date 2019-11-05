package com.sdmproject.service;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.repository.RoleRepository;
import com.sdmproject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserService {

    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
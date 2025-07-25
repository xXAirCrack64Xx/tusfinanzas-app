package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService implements IUserService {

    @Inject
    UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}


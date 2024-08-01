package com.banorte.application.service;

import java.util.List;
import java.util.UUID;

import com.banorte.application.port.input.UserUseCase;
import com.banorte.application.port.output.UserRepository;
import com.banorte.domain.User;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class UserService implements UserUseCase {

    @Inject
    UserRepository userRepository;

    @Override
    public User createUser(String name) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(name);
        return userRepository.save(user);
    }

    @Override
    public Uni<User> getUser(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Uni<List<User>> getAll() {
        return userRepository.findAll();
    }
}

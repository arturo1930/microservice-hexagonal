package com.banorte.application.port.input;

import java.util.List;

import com.banorte.domain.User;

import io.smallrye.mutiny.Uni;

public interface UserUseCase {
    User createUser(String name);
    Uni<User> getUser(String id);
    Uni<List<User>> getAll();
}
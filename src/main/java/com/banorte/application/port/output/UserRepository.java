package com.banorte.application.port.output;
import java.util.List;

import com.banorte.domain.User;

import io.smallrye.mutiny.Uni;

public interface UserRepository {
    User save(User user);
    Uni<User> findById(String id);
    Uni<List<User>> findAll();
}
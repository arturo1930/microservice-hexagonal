package com.banorte.adapter.output;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.banorte.application.port.output.UserRepository;
import com.banorte.domain.User;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Uni<User> findById(String id) {
        return Uni.createFrom().item(() -> users.get(id));
    }

    @Override
    public Uni<List<User>> findAll() {
        return Uni.createFrom().item(users.values().stream().toList());
    }
    
}

package com.davivienda.tusfinanzas.repository;

import jakarta.enterprise.context.ApplicationScoped;
import com.davivienda.tusfinanzas.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>, IUserRepository {

    @Override
    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }
}

package com.davivienda.tusfinanzas.repository;

import com.davivienda.tusfinanzas.entity.User;

public interface IUserRepository {
    User findByUsername(String username);
}
package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.entity.User;

public interface IUserService {
    User findByUsername(String username);
}

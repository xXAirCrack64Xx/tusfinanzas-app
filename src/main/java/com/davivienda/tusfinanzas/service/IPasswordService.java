package com.davivienda.tusfinanzas.service;

public interface IPasswordService {
    String hashPassword(String password);
    boolean verifyPassword(String plain, String hashed);
}

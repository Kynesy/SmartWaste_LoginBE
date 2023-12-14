package it.unisalento.pas.loginbe.services;

import it.unisalento.pas.loginbe.models.User;

public interface IUserService {
    boolean existByUsername(String username);
    boolean existByEmail(String email);
    User createUser(User user);
    int deleteById(String userId);
}

package it.unisalento.pas.loginbe.services;

import it.unisalento.pas.loginbe.domain.User;

public interface IUserService {
    int saveUser(User user);
    int userExist(String userID);
}

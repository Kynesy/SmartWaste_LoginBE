package it.unisalento.pas.loginbe.services;

import it.unisalento.pas.loginbe.domain.User;

public interface IUserService {
    int createUser(User user);
    int deleteUser(String userId);
    boolean userExist(String userId);
    User getUser(String email, String applicationId);
    int checkCredentials(User user);
}

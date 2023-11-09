package it.unisalento.pas.loginbe.services;

import it.unisalento.pas.loginbe.domain.User;

public interface IUserService {
    int createUser(User user);
    boolean userExist(String userId);
    User getUser(String email, String applicationId);
    int checkCredentials(User user);
}

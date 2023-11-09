package it.unisalento.pas.loginbe.utils;

import it.unisalento.pas.loginbe.domain.User;

import java.util.Map;

public interface IJwtGenerator {
    String generateToken(User user);
    String generateLoginJSON(User user);
}

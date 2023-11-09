package it.unisalento.pas.loginbe.utils;

import io.jsonwebtoken.Claims;
import it.unisalento.pas.loginbe.domain.User;

import java.util.Map;

public interface IJwtGenerator {
    String generateToken(User user);
    String generateLoginJSON(User user);
    Claims decodeToken(String token);
    boolean verifyToken(String token);
}

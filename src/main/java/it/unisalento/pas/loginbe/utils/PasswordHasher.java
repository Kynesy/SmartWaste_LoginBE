package it.unisalento.pas.loginbe.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

public class PasswordHasher implements IPasswordHasher {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean checkPassword(String hashedPass, String password) {
        return passwordEncoder.matches(password, hashedPass);
    }
}

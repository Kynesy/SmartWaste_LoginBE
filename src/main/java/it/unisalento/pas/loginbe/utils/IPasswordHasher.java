package it.unisalento.pas.loginbe.utils;

public interface IPasswordHasher {
    String hashPassword(String password);
    boolean checkPassword(String hashedPass, String password);
}

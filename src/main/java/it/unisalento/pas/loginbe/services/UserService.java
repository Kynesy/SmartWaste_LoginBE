package it.unisalento.pas.loginbe.services;

import it.unisalento.pas.loginbe.models.User;
import it.unisalento.pas.loginbe.respositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    @Autowired
    IUserRepository userRepository;

    @Override
    public boolean existByUsername(String username) {
        return false;
    }

    @Override
    public boolean existByEmail(String email) {
        return false;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public int deleteById(String userId) {
        if(!userRepository.existsById(userId)){
            return 1;
        }

        userRepository.deleteById(userId);
        return 0;
    }
}

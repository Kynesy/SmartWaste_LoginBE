package it.unisalento.pas.loginbe.services;

import it.unisalento.pas.loginbe.domain.User;
import it.unisalento.pas.loginbe.repository.IUserRepository;
import it.unisalento.pas.loginbe.utils.IPasswordHasher;
import it.unisalento.pas.loginbe.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher = new PasswordHasher();
    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int saveUser(User user) {
        user.setPassword(passwordHasher.hashPassword(user.getPassword()));
        this.userRepository.save(user);
        return 0;
    }

    @Override
    public int userExist(String userID) {
        if(this.userRepository.existsById(userID)){
            return 1;
        } else{
            return 0;
        }
    }
}

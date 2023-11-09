package it.unisalento.pas.loginbe.services;

import it.unisalento.pas.loginbe.domain.User;
import it.unisalento.pas.loginbe.repository.IUserRepository;
import it.unisalento.pas.loginbe.utils.IPasswordHasher;
import it.unisalento.pas.loginbe.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public int createUser(User user) {
        if(!userRepository.existsByEmailAndApplicationId(user.getEmail(), user.getApplicationId())){
            user.setPassword(passwordHasher.hashPassword(user.getPassword()));
            this.userRepository.save(user);
            return 0;
        }
        return 1;
    }



    @Override
    public boolean userExist(String userID) {
        return this.userRepository.existsById(userID);
    }

    @Override
    public int checkCredentials(User user) {
        User userDB = userRepository.findByEmailAndApplicationId(user.getEmail(), user.getApplicationId());
        if(userDB == null){
            return 1;
        }

        if (passwordHasher.checkPassword(userDB.getPassword(), user.getPassword())){
            user.setId(userDB.getId());
            return 0;
        }

        return 2;
    }

    @Override
    public User getUser(String email, String applicationId) {
        return userRepository.findByEmailAndApplicationId(email, applicationId);
    }

    @Override
    public int deleteUser(String userId) {
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
            return 0;
        }
        return 1;
    }
}

package it.unisalento.pas.loginbe.repository;

import it.unisalento.pas.loginbe.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUserRepository extends MongoRepository<User, String> {
    User findByEmailAndApplicationId(String email, String applicationId);
    boolean existsByEmailAndApplicationId(String email, String applicationId);
}

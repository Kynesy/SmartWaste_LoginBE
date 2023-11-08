package it.unisalento.pas.loginbe.controllers;

import it.unisalento.pas.loginbe.domain.User;
import it.unisalento.pas.loginbe.dto.UserDTO;
import it.unisalento.pas.loginbe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        User user = fromUserDTOtoUser(userDTO);

        System.out.println("USER: " + user.getApplicationId());
        System.out.println("USER: " + user.getEmail());
        System.out.println("USER: " + user.getPassword());
        System.out.println("USER: " + user.getId());

        int result = userService.saveUser(user);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"User created successfully\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"User creation failed\"}");
        }
    }

    private User fromUserDTOtoUser(UserDTO userDTO) {
        User user = new User();

        System.out.println("DTO: " + userDTO.getApplicationId());
        System.out.println("DTO: " + userDTO.getEmail());
        System.out.println("DTO: " + userDTO.getPassword());
        System.out.println("DTO: " + userDTO.getId());

        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setApplicationId(userDTO.getApplicationId());

        return user;
    }

    @GetMapping("/exist/{userID}")
    public ResponseEntity<Boolean> existUser(@PathVariable String userID) {
        if (userService.userExist(userID) == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}

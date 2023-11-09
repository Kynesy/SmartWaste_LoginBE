package it.unisalento.pas.loginbe.controllers;

import it.unisalento.pas.loginbe.domain.User;
import it.unisalento.pas.loginbe.dto.UserDTO;
import it.unisalento.pas.loginbe.security.AuthorizedApplications;
import it.unisalento.pas.loginbe.services.UserService;
import it.unisalento.pas.loginbe.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtGenerator jwtGenerator = new JwtGenerator();
    private final AuthorizedApplications authorizer = new AuthorizedApplications();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        User user = fromUserDTOtoUser(userDTO);

        if(authorizer.isApplicationIDnotValid(user.getApplicationId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Application ID not valid");
        }

        int result = userService.createUser(user);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(jwtGenerator.generateLoginJSON(user));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"User creation failed\"}");
        }
    }

    @GetMapping("/exist/{userID}")
    public ResponseEntity<Boolean> existUser(@PathVariable String userID) {
        if (userService.userExist(userID)) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO){
        User user = fromUserDTOtoUser(userDTO);

        if(authorizer.isApplicationIDnotValid(user.getApplicationId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Application ID not valid");
        }

        int result = userService.checkCredentials(user);
        String responseMessage;

        return switch (result) {
            case 0 -> {
                responseMessage = jwtGenerator.generateLoginJSON(userService.getUser(user.getEmail(), user.getApplicationId()));
                yield ResponseEntity.ok(responseMessage);
            }
            case 2 -> {
                responseMessage = "Wrong credentials.";
                yield ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMessage);
            }
            default -> {
                responseMessage = "Utente non esistente o altro.";
                yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
            }
        };
    }

    private User fromUserDTOtoUser(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setApplicationId(userDTO.getApplicationId());
        user.setRole(userDTO.getRole());

        return user;
    }
}

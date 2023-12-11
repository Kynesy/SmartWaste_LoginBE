package it.unisalento.pas.loginbe.controllers;

import java.util.List;
import java.util.stream.Collectors;

import it.unisalento.pas.loginbe.models.User;
import it.unisalento.pas.loginbe.models.UserDetailsCustom;
import it.unisalento.pas.loginbe.models.requests.LoginRequest;
import it.unisalento.pas.loginbe.models.requests.SignupRequest;
import it.unisalento.pas.loginbe.models.responses.JwtResponse;
import it.unisalento.pas.loginbe.models.responses.MessageResponse;
import it.unisalento.pas.loginbe.models.responses.SignUpResponse;
import it.unisalento.pas.loginbe.respositories.IUserRepository;
import it.unisalento.pas.loginbe.respositories.IUserRepository;
import it.unisalento.pas.loginbe.utils.JwtUtils;
import it.unisalento.pas.loginbe.services.UserDetailsCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwtToken(authentication, roles.get(0));


        JwtResponse jwtResponse = new JwtResponse();

        jwtResponse.setToken(jwt);
        jwtResponse.setId(userDetails.getId());
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setEmail(userDetails.getEmail());
        jwtResponse.setRole(roles.get(0));

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();

        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());

        user = userRepository.save(user);

        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setId(user.getId());
        signUpResponse.setMessage("Sign-up successful");

        return ResponseEntity.status(HttpStatus.OK).body(signUpResponse);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        if(!userRepository.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User ID not found."));
        }

        userRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User deleted successfully"));
    }
}


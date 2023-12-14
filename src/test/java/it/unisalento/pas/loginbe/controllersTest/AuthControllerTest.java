package it.unisalento.pas.loginbe.controllersTest;

import com.nimbusds.jose.shaded.gson.Gson;


import it.unisalento.pas.loginbe.models.User;
import it.unisalento.pas.loginbe.models.UserDetailsCustom;
import it.unisalento.pas.loginbe.models.requests.LoginRequest;
import it.unisalento.pas.loginbe.models.requests.SignupRequest;
import it.unisalento.pas.loginbe.models.responses.JwtResponse;
import it.unisalento.pas.loginbe.respositories.IUserRepository;
import it.unisalento.pas.loginbe.services.IUserService;
import it.unisalento.pas.loginbe.services.UserDetailsCustomService;
import it.unisalento.pas.loginbe.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IUserService userService;

    @MockBean
    PasswordEncoder encoder;

    @MockBean
    UserDetailsCustomService userDetailsCustomService;

    @Autowired
    AuthenticationManager authenticationManager;

    @MockBean
    JwtUtils jwtUtils;

    @Test
    void authenticateUserTest() throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "testPassword");

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetailsCustom userDetails = new UserDetailsCustom("1", "testUser", "test@example.com", "password", authorities);
        when(userDetailsCustomService.loadUserByUsername(loginRequest.getUsername())).thenReturn(userDetails);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtUtils.generateJwtToken(any(), any())).thenReturn("mockToken");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void registerUserTest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("new@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole("ROLE_USER");

        Gson gson = new Gson();
        String json = gson.toJson(signupRequest);

        User user = new User();
        user.setId("mockId");
        user.setUsername("newUser");
        user.setEmail("new@example.com");
        user.setPassword("encodedPassword");
        user.setRole("ROLE_USER");

        when(userService.existByUsername("newUser")).thenReturn(false);
        when(userService.existByEmail("new@example.com")).thenReturn(false);
        when(encoder.encode("password")).thenReturn("encodedPassword");
        when(userService.createUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserTest() throws Exception {
        String userID = "mockUserID";

        when(userService.deleteById(userID)).thenReturn(0);

        mockMvc.perform(delete("/api/auth/delete/{id}", userID)
                        .with(user("user").authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

}

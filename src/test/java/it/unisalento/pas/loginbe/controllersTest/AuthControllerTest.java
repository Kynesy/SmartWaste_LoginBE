package it.unisalento.pas.loginbe.controllersTest;

import com.nimbusds.jose.shaded.gson.Gson;


import it.unisalento.pas.loginbe.models.User;
import it.unisalento.pas.loginbe.models.requests.LoginRequest;
import it.unisalento.pas.loginbe.models.requests.SignupRequest;
import it.unisalento.pas.loginbe.models.responses.JwtResponse;
import it.unisalento.pas.loginbe.respositories.IUserRepository;
import it.unisalento.pas.loginbe.services.IUserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken("mockToken");
        jwtResponse.setId("mockId");
        jwtResponse.setUsername("testUser");
        jwtResponse.setEmail("test@example.com");
        jwtResponse.setRole("ROLE_USER");

        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication); // Mock authentication result

        when(jwtUtils.generateJwtToken(authentication, "ROLE_USER"))
                .thenReturn("mockToken"); // Mock JWT token generation

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
        when(userService.deleteById("mockId")).thenReturn(0);

        mockMvc.perform(delete("/api/auth/delete/mockId"))
                .andExpect(status().isOk());
    }

}

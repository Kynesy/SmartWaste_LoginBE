package it.unisalento.pas.loginbe.models.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignupRequest {
    private String username;
    private String email;
    private String role;
    private String password;
}

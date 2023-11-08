package it.unisalento.pas.loginbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String id;
    private String applicationId;
    private String email;
    private String password;
}

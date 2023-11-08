package it.unisalento.pas.loginbe.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Questa classe rappresenta un utente nel sistema.
 */
@Document("users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    /**
     * Identificatore univoco dell'utente.
     */
    @Id private String id;

    /**
     * Applicazione a cui appartiene l'utente.
     */
    private String applicationId;

    /**
     * Indirizzo email dell'utente.
     */
    private String email;

    /**
     * Password dell'utente.
     */
    private String password;
}

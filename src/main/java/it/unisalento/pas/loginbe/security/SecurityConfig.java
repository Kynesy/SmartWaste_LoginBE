package it.unisalento.pas.loginbe.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configurazione della sicurezza per l'applicazione.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configura le regole di sicurezza per le richieste HTTP.
     *
     * @param http Oggetto HttpSecurity da configurare
     * @return SecurityFilterChain configurato
     * @throws Exception Eccezione in caso di errori di configurazione
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });
        http.csrf(AbstractHttpConfigurer::disable);
        // Solo gli utenti autorizzati possono accedere alle API
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // UserController
                        .requestMatchers(HttpMethod.GET, "/api/user/exist/{userID}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/create").permitAll()
                        .anyRequest().denyAll())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /**
     * Configura le impostazioni CORS (Cross-Origin Resource Sharing).
     *
     * @return Oggetto CorsConfigurationSource configurato
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

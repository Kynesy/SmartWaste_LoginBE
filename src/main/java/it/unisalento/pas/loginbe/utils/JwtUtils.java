package it.unisalento.pas.loginbe.utils;


import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import it.unisalento.pas.loginbe.models.UserDetailsCustom;
import it.unisalento.pas.loginbe.services.UserDetailsCustomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${loginBE.app.jwtSecret}")
    private String jwtSecret;

    @Value("${loginBE.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication, String role) {

        UserDetailsCustom userPrincipal = (UserDetailsCustom) authentication.getPrincipal();

        Claims claims = Jwts.claims();
        claims.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs));
        claims.setIssuedAt(new Date());
        claims.setSubject(userPrincipal.getUsername());
        claims.put("role", role);


        return Jwts.builder()
                .setClaims(claims)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}


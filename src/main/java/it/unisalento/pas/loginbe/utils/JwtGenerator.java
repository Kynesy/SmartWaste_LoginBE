package it.unisalento.pas.loginbe.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.unisalento.pas.loginbe.domain.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtGenerator implements IJwtGenerator{
    private String secret = "NsCz8OVJ8JJN57V7LUdxqpKQIQoby5OQEo+Dw+wUfg3VbOS61Z9YP38Rw/P7twE+";
    private long expirationTime = 36000000; // In milliseconds

    @Override
    public String generateToken(User user) {
        if (user.getEmail() == null) {
            return null;
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getId());
        claims.put("role", user.getRole());

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuer("LoginBE")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

       return jwtToken;
    }

    @Override
    public String generateLoginJSON(User user) {
        Map<String, String> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("email", user.getEmail());
        data.put("token", this.generateToken(user));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

package edu.tum.ase.authService.jwt;

import edu.tum.ase.backendCommon.jwt.JwtUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExtendedJwtUtil extends JwtUtil {

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    public String generateBearerToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return createBearerToken(claims, userDetails.getUsername());
    }

    // Create JWS with both custom and registered claims, signed by
    // a private key.
    private String createToken(Map<String, Object> claims, String subject) {
        return generateBaseToken(claims, subject)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5)) // Expires after 5 hours
                .compact();
    }

    private String createBearerToken(Map<String, Object> claims, String subject) {
        return generateBaseToken(claims, subject)
                .setExpiration(null)
                .compact();
    }

    private JwtBuilder generateBaseToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("asedelivery")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(keyStoreManager.getPrivateKey(), SignatureAlgorithm.RS256);
    }
}

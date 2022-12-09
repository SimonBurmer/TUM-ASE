package edu.tum.ase.backendCommon.jwt;

import com.nimbusds.jose.crypto.RSADecrypter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Autowired
    protected KeyStoreManager keyStoreManager;

    // Create a Parser to read info inside a JWT. This parser use the public key
    // to verify the signature of incoming JWT tokens
    private JwtParser loadJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(keyStoreManager.getPublicKey())
                .build();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<Map<String, String>> extractAuthorities(String token) {
        //noinspection unchecked
        return (List<Map<String, String>>) extractClaim(token, c -> c.get("roles", Object.class));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return loadJwtParser()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Check if the JWT is signed by us, and is not expired
    public boolean verifyJwtSignature(String token) {
        // DONE: Check if the JWT is signed by the public key in the KeyStore
        // and the JWT is not expired
        return loadJwtParser().isSigned(token) && !isTokenExpired(token);
    }

    public String decryptPasswordInJwe(String password) {
        RSADecrypter decrypter = new RSADecrypter((RSAPrivateKey) keyStoreManager.getPrivateKey());
        String decryptedPw = "";
        /*try {*/
            // TODO: Parse the encrypted JWE
            // TODO: Decrypt the JWE and extract the "password" info

            Claims claims = extractAllClaims(password);

            System.out.println(claims);
        /*} catch (JOSEException | ParseException e) {
            e.printStackTrace();
        }*/
        return decryptedPw;
    }
}

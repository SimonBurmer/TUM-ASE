package edu.tum.ase.casService.service;

import edu.tum.ase.backendCommon.jwt.JwtUtil;
import edu.tum.ase.backendCommon.jwt.KeyStoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;

@Service
public class AuthService {


    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private MongoUserDetailsService mongoUserDetailsService;

    /*public ResponseEntity<String> authenticateUser(
            String authHeader,
            HttpServletRequest request) // why do we need request here?
    {
        String username = "";
        String password = "";

        // DONE: Get the username and password by decoding the Base64 credential inside
        // the Basic Authentication
        authHeader = authHeader.substring(6);
        authHeader = new String(Base64.getDecoder().decode(authHeader));

        String[] split = authHeader.split(":");

        username = split[0];
        password = split[1];

        // DONE: find if there is any user exists in the database based on the credential,
        // and authenticate the user using the Spring Authentication Manager
        UserDetails userDetails = mongoUserDetailsService.loadUserByUsername(username);
        setAuthentication(userDetails, password);

        return ResponseEntity.of(Optional.of("Hello " + username));
    }*/

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<String> authenticateUser(
            String authorization,
            HttpServletRequest request) throws Exception {

        String authHeader = request.getHeader("Authorization");

        // DONE: Get the username and password by decoding the Base64 credential inside
        // the Basic Authentication
        authHeader = authHeader.substring(6);
        authHeader = new String(Base64.getDecoder().decode(authHeader));

        String[] split = authHeader.split(":");

        String username = split[0];
        String password = split[1];


        // DONE: find if there is any user exists in the database based on the credential.
        // Hint: What do you get from Mission 3?
        final UserDetails userDetails = mongoUserDetailsService.loadUserByUsername(username);

        if (bcryptPasswordEncoder.matches(password,
                userDetails.getPassword())) {
            // Remove the setAuthentication() as we do not want to keep the
            // user authenticated in the upcoming requests. The user has to attach
            // our generated JWT for every request they sent to the server.
            // setAuthentication(userDetails, request);
            // DONE: Generate a JWT token based on the info of the authenticated user
            final String jwt = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Username or password is incorrect",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void setAuthentication(User userDetails, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        // DONE: Get the username and password by decoding the Base64 credential inside
        // the Basic Authentication
        authHeader = authHeader.substring(6);
        authHeader = new String(Base64.getDecoder().decode(authHeader));

        String[] split = authHeader.split(":");

        String username = split[0];
        String password = split[1];

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password);
        authentication = authManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Autowired
    KeyStoreManager keyStoreManager;

    public HashMap<String, String> getPublicKeyData() throws Exception {
        HashMap<String, String> pKeyResponse = new HashMap<String, String>();
        RSAPublicKey rsaPubKey = (RSAPublicKey) keyStoreManager.getPublicKey();
        byte[] modulusByte = rsaPubKey.getModulus().toByteArray();// DONE: Get Modulus of Public key;

        // Format the modulus into byte format (e.g. AB:CD:E1)
        String modulusByteStr = "";
        for (byte b : modulusByte) {
            modulusByteStr += String.format(":%02x", b);
        }
        modulusByteStr = modulusByteStr.substring(1);

        byte[] publicKeyByte = rsaPubKey.getEncoded(); // TODO: not sure if this is necessary
        String publicKeyByteString = "";
        for (byte b : publicKeyByte) {
            publicKeyByteString += String.format(":%02x", b);
        }
        publicKeyByteString = publicKeyByteString.substring(1);

        pKeyResponse.put("key", publicKeyByteString);// DONE: Get encoded public key);
        pKeyResponse.put("n", modulusByteStr);
        pKeyResponse.put("e", String.valueOf(rsaPubKey.getPublicExponent())); // DONE: Get public exponent);
        return pKeyResponse;
    }

    public String decryptPassword(String token) {
        return jwtUtil.decryptPasswordInJwe(token);
    }
}

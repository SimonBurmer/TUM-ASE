package edu.tum.ase.casService.service;

import edu.tum.ase.backendCommon.jwt.KeyStoreManager;
import edu.tum.ase.casService.jwt.ExtendedJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private ExtendedJwtUtil jwtUtil;


    public ResponseEntity<String> authenticateUser(String authorization, HttpServletResponse response) {

        if (!authorization.startsWith("Basic ")) {
            return new ResponseEntity<>("Did not find a basic auth header",
                    HttpStatus.BAD_REQUEST);
        }

        authorization = authorization.substring(6);
        authorization = new String(Base64.getDecoder().decode(authorization));

        String[] split = authorization.split(":");

        String username = split[0];
        String password = split[1];


        // DONE: find if there is any user exists in the database based on the credential.
        // Hint: What do you get from Mission 3?
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (bcryptPasswordEncoder.matches(password,
                userDetails.getPassword())) {
            final String jwt = jwtUtil.generateToken(userDetails);

            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(3600); // TODO: check this
            response.addCookie(jwtCookie);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Username or password is incorrect",
                    HttpStatus.BAD_REQUEST
            );
        }
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

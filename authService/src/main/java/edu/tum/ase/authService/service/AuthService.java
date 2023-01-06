package edu.tum.ase.authService.service;

import edu.tum.ase.authService.request.CreateBearerRequest;
import edu.tum.ase.authService.response.CreateBearerResponse;
import edu.tum.ase.backendCommon.jwt.KeyStoreManager;
import edu.tum.ase.authService.jwt.ExtendedJwtUtil;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private ExtendedJwtUtil jwtUtil;


    public ResponseEntity<String> authenticateUser(String email, String password_enc, boolean remember, HttpServletResponse response) {
        String password = jwtUtil.decryptJwe(password_enc);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (bcryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            String jwt = jwtUtil.generateToken(userDetails);

            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(remember ? 60 * 60 * 24 * 2 : -1);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Username or password is incorrect",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public ResponseEntity<String> authenticateUser(String basicAuthHeader, HttpServletResponse response) {

        if (!basicAuthHeader.startsWith("Basic ")) {
            return new ResponseEntity<>("Did not find a basic auth header",
                    HttpStatus.BAD_REQUEST);
        }

        basicAuthHeader = basicAuthHeader.substring(6);
        basicAuthHeader = new String(Base64.getDecoder().decode(basicAuthHeader));

        String[] split = basicAuthHeader.split(":");

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
            jwtCookie.setMaxAge(-1);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Username or password is incorrect",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public ResponseEntity<CreateBearerResponse> createBearerToken(CreateBearerRequest request) {
        UserDetails boxUser = new org.springframework.security.core.userdetails.User(request.getId(),
                "",
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(UserRole.getCompleteRole(UserRole.RASPI))));
        String token = jwtUtil.generateBearerToken(boxUser);
        CreateBearerResponse response = new CreateBearerResponse(request.getId(), token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Autowired
    KeyStoreManager keyStoreManager;

    public HashMap<String, String> getPublicKeyData() throws Exception {
        HashMap<String, String> pKeyResponse = new HashMap<String, String>();
        RSAPublicKey rsaPubKey = (RSAPublicKey) keyStoreManager.getPublicKey();
        byte[] modulusByte = rsaPubKey.getModulus().toByteArray();// DONE: Get Modulus of Public key;

        // Format the modulus into byte format (e.g. AB:CD:E1)
        StringBuilder modulusByteStr = new StringBuilder();
        for (byte b : modulusByte) {
            modulusByteStr.append(String.format(":%02x", b));
        }
        modulusByteStr = new StringBuilder(modulusByteStr.substring(1));

        byte[] publicKeyByte = rsaPubKey.getEncoded();
        StringBuilder publicKeyByteString = new StringBuilder();
        for (byte b : publicKeyByte) {
            publicKeyByteString.append(String.format(":%02x", b));
        }
        publicKeyByteString = new StringBuilder(publicKeyByteString.substring(1));

        pKeyResponse.put("key", publicKeyByteString.toString());// DONE: Get encoded public key);
        pKeyResponse.put("n", modulusByteStr.toString());
        pKeyResponse.put("e", String.valueOf(rsaPubKey.getPublicExponent())); // DONE: Get public exponent);
        return pKeyResponse;
    }
}

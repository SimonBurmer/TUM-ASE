package edu.tum.ase.casService.controller;

import edu.tum.ase.casService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    // DONE: Implement Authentication of the user credentials
    public ResponseEntity<String> authenticate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, HttpServletRequest request) throws Exception {
        String authorization = "?";
        return authService.authenticateUser(authorization, request);
    }

    @GetMapping("/pkey")
    public ResponseEntity<HashMap<String, String>> getPublicKey() throws Exception {
        HashMap<String, String> pKeyData = authService.getPublicKeyData();
        return new ResponseEntity<HashMap<String, String>>(pKeyData, HttpStatus.OK);
    }

    @PostMapping("/enc")
    public HttpStatus testJWEToken(@RequestBody String token) {
        authService.decryptPassword(token);
        return HttpStatus.OK;
    }

}
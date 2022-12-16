package edu.tum.ase.authService.controller;

import edu.tum.ase.authService.request.AuthRequest;
import edu.tum.ase.authService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("")
    // DONE: Implement Authentication of the user credentials
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody AuthRequest authRequest, HttpServletResponse response) {
        return authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword_enc(), response);
    }

    @PostMapping("legacy") // TODO: remove this
    // DONE: Implement Authentication of the user credentials
    public ResponseEntity<String> authenticateUserLegacy(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, HttpServletResponse response) {
        return authService.authenticateUser(authorization, response);
    }

    @GetMapping("/pkey")
    public ResponseEntity<HashMap<String, String>> getPublicKey() throws Exception {
        HashMap<String, String> pKeyData = authService.getPublicKeyData();
        return new ResponseEntity<>(pKeyData, HttpStatus.OK);
    }
}
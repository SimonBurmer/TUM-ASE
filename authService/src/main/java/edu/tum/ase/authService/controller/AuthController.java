package edu.tum.ase.authService.controller;

import edu.tum.ase.authService.request.AuthRequest;
import edu.tum.ase.authService.request.CreateBearerRequest;
import edu.tum.ase.authService.response.CreateBearerResponse;
import edu.tum.ase.authService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody AuthRequest authRequest, HttpServletResponse response) {
        return authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword_enc(), authRequest.isRemember(), response);
    }

    @PostMapping("legacy") // TODO: remove this
    // DONE: Implement Authentication of the user credentials
    public ResponseEntity<String> authenticateUserLegacy(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, HttpServletResponse response) {
        return authService.authenticateUser(authorization, response);
    }

    @PreAuthorize("hasRole('DISPATCHER')")
    @PostMapping("bearer")
    public ResponseEntity<CreateBearerResponse> generateBoxBearerToken(@RequestHeader(HttpHeaders.COOKIE) String cookie, @Valid @RequestBody CreateBearerRequest request) {
        return authService.createBearerToken(request);
    }

    @GetMapping("/pkey")
    public ResponseEntity<HashMap<String, String>> getPublicKey() throws Exception {
        HashMap<String, String> pKeyData = authService.getPublicKeyData();
        return new ResponseEntity<>(pKeyData, HttpStatus.OK);
    }
}
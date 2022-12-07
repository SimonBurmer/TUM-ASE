package edu.tum.ase.casService.filter;

import edu.tum.ase.backendCommon.jwt.JwtUtil;
import edu.tum.ase.casService.service.MongoUserDetailsService;
import edu.tum.ase.casService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MongoUserDetailsService mongoUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String username = null;
        String jwt = null;
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authenticate Header " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            // DONE: Get the JWT in the header.
            // DONE: If the JWT expires or not signed by us, send an error to the client
            // DONE: Extract the username from the JWT token.
            jwt = authHeader.substring(7);
            if (!jwtUtil.verifyJwtSignature(jwt)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "JWT Token is not valid");
            }
            username = jwtUtil.extractUsername(jwt);
        } else {
            // No valid authentication, No go
            if (authHeader == null || !authHeader.startsWith("Basic")) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "No JWT Token or Basic Auth Info Found");
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // DONE: load a user from the database that has the same username
            // as in the JWT token.
            User userDetails = (User) mongoUserDetailsService.loadUserByUsername(username);
            authService.setAuthentication(userDetails, request);
            Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
            System.out.printf("Authenticate Token Set:\n"
                            + "Username: %s\n"
                            + "Password: %s\n"
                            + "Authority: %s\n%n",
                    authContext.getPrincipal(),
                    authContext.getCredentials(),
                    authContext.getAuthorities().toString());
        }
        filterChain.doFilter(request, response);
    }
}

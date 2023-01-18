package edu.tum.ase.backendCommon.filter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class FilterUtil {

    public static void setSecurityContextFromUsername(String username, List<SimpleGrantedAuthority> authorityList) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = new User(username, "", authorityList);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorityList);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

    }

    public static boolean verifyUser(String username, String role, RestTemplate restTemplate) {
        try {
            ResponseEntity<String> response = restTemplate.exchange("lb://AUTH-SERVICE/intern/user/" + username, HttpMethod.GET, null,  String.class);
            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().equals(role);
        } catch (HttpClientErrorException e) {
            return false;
        }
    }
}

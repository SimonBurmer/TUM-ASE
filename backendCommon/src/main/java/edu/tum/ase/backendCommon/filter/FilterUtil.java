package edu.tum.ase.backendCommon.filter;

import edu.tum.ase.backendCommon.model.AseUser;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
            ResponseEntity<AseUser> response = restTemplate.exchange("lb://AUTH-SERVICE/intern/user/" + username, HttpMethod.GET, null, AseUser.class);
            AseUser user = response.getBody();
            return response.getStatusCode() == HttpStatus.OK && user != null && UserRole.getCompleteRole(user.getRole()).equals(role);
        } catch (HttpClientErrorException e) {
            return false;
        }
    }
}

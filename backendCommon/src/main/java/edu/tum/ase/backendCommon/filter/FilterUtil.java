package edu.tum.ase.backendCommon.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class FilterUtil {

    public static void setSecurityContextFromUsername(String username, List<SimpleGrantedAuthority> authorityList) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = new User(username, "", authorityList);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorityList);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

    }
}

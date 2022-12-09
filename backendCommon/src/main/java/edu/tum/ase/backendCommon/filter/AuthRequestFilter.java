package edu.tum.ase.backendCommon.filter;

import edu.tum.ase.backendCommon.jwt.JwtUtil;
import edu.tum.ase.backendCommon.roles.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AuthRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public AuthRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final List<String> excluded_urls = List.of("/auth/**");
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String url = request.getRequestURI();
        return excluded_urls.stream().anyMatch(x -> antPathMatcher.match(x, url));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "no cookies found");
            return;
        }

        Optional<Cookie> maybeJwt = Arrays.stream(cookies).filter(c -> c.getName().equals("jwt")).findFirst();
        if (maybeJwt.isEmpty()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "no jwt cookie found");
            return;
        }

        Cookie jwtCookie = maybeJwt.get();
        String jwt = jwtCookie.getValue();

        if (!jwtUtil.verifyJwtSignature(jwt)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "jwt is not valid");
            return;
        }

        String username = jwtUtil.extractUsername(jwt);
        List<Map<String, String>> authorities = jwtUtil.extractAuthorities(jwt);

        List<SimpleGrantedAuthority> authorityList = authorities.stream()
                .flatMap(s -> s.values().stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = new User(username, "" /*TODO: check if password is necessary here*/, authorityList);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorityList);
            SecurityContextHolder.getContext().setAuthentication(authentication);

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

package edu.tum.ase.backendCommon.filter;

import edu.tum.ase.backendCommon.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AuthRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public AuthRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final List<String> excluded_urls = List.of("/auth/**");
    private static final List<String> explicitly_included_urls = List.of("/auth/bearer");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String url = request.getRequestURI();
        if (explicitly_included_urls.stream().noneMatch(e -> antPathMatcher.match(e, url))) {
            return excluded_urls.stream().anyMatch(x -> antPathMatcher.match(x, url));
        } else {
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<Cookie> maybeJwt = Arrays.stream(cookies).filter(c -> c.getName().equals("jwt")).findFirst();
        if (maybeJwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie jwtCookie = maybeJwt.get();
        String jwt = jwtCookie.getValue();

        if (!jwtUtil.verifyJwtSignature(jwt)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "jwt is not valid");
            return;
        }

        List<SimpleGrantedAuthority> authorityList = jwtUtil.extractAuthorities(jwt);
        String username = jwtUtil.extractUsername(jwt);
        FilterUtil.setSecurityContextFromUsername(username, authorityList);

        filterChain.doFilter(request, response);
    }
}

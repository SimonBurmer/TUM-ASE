package edu.tum.ase.backendCommon.filter;

import edu.tum.ase.backendCommon.jwt.JwtUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BearerRequestFilter extends OncePerRequestFilter {

    private final ApplicationContext applicationContext;

    private static final String HEADER_START = "Bearer ";

    private static final List<String> INCLUDED_URLS = List.of("/box/**", "/delivery/**", "/rfid/**");

    public BearerRequestFilter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String url = request.getRequestURI();
        return INCLUDED_URLS.stream().noneMatch(e -> antPathMatcher.match(e, url));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            //response.sendError(HttpStatus.UNAUTHORIZED.value(), "no authorization information in header");
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorizationHeader.startsWith(HEADER_START)) {
            //response.sendError(HttpStatus.UNAUTHORIZED.value(), "no bearer information in header");
            filterChain.doFilter(request, response);
            return;
        }

        JwtUtil jwtUtil = ((JwtUtil) applicationContext.getBean("jwtUtil"));
        String jwt = authorizationHeader.substring(HEADER_START.length() - 1);

        if (!jwtUtil.verifyJwtSignature(jwt)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "jwt is not valid");
            return;
        }

        List<SimpleGrantedAuthority> authorityList = jwtUtil.extractAuthorities(jwt);
        String username = jwtUtil.extractUsername(jwt);
        FilterUtil.setSecurityContextFromUsername(username, authorityList);

        Optional<SimpleGrantedAuthority> maybeRole = authorityList.stream().filter((r) -> r.getAuthority().startsWith("ROLE_")).findFirst();
        if (maybeRole.isEmpty()) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "there is no role stored in jwt");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

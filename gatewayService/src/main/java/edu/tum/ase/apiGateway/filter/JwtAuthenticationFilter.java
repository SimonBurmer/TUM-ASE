package edu.tum.ase.apiGateway.filter;

import edu.tum.ase.backendCommon.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter implements GatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public static class Config {}

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            String authorization = request.getHeaders().getOrEmpty("Authorization").get(0);

            if (!authorization.startsWith("Bearer ")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            String token = authorization.substring(7);
            if (!jwtUtil.verifyJwtSignature(token)) {

                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);

                return response.setComplete();
            }

            return chain.filter(exchange);
        };
    }
}

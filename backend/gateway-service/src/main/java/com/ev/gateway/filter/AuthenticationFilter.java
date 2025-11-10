package com.ev.gateway.filter;

import com.ev.gateway.util.JwtUtil; // (File 4)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator; // (File 3)

    @Autowired
    private JwtUtil jwtUtil; // (File 4)

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Kiểm tra xem route này có cần bảo vệ không (vd: /login thì không)
            if (validator.isSecured.test(request)) {

                // 2. Kiểm tra xem có header "Authorization" không
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return this.onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7); // Bỏ "Bearer "
                }

                try {
                    // 3. Xác thực token (dùng JwtUtil)
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    // Lỗi: Token không hợp lệ
                    return this.onError(exchange, HttpStatus.UNAUTHORIZED);
                }
            }

            // 4. Nếu route là public (hoặc token hợp lệ) -> cho đi tiếp
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
        // (Để trống)
    }
}
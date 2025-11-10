package com.ev.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // Danh sách các API public, không cần token
    public static final List<String> publicApiEndpoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> publicApiEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
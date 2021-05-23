package com.vpnbeast.apigateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> AUTH_WHITELIST = List.of(
            "/auth/authenticate",
            "/users/register",
            "/users/reset-password",
            "/users/verify",
            "/users/resend-verification-code"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> AUTH_WHITELIST
            .stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

}
package com.vpnbeast.gatewayservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface HttpService {

    Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus);
    String getAuthHeader(ServerHttpRequest request);
    Boolean isAuthMissing(ServerHttpRequest request);
    void populateRequestWithHeaders(ServerWebExchange exchange, String[] roles, String username);

}

package com.vpnbeast.gatewayservice.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpnbeast.gatewayservice.model.ExceptionInfo;
import com.vpnbeast.gatewayservice.service.JwtService;
import com.vpnbeast.gatewayservice.util.DateUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)) {
            if (isAuthMissing(request))
                return onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            final String token = getAuthHeader(request).substring(7);

            if (!jwtService.isTokenValid(token)) {
                log.info("token is invalid");
                return onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }

            // TODO: check if token is expired

            populateRequestWithHeaders(exchange, token, jwtService.getUsernameFromToken(token));
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        try {
            ServerHttpResponse response = exchange.getResponse();
            DataBuffer db = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(ExceptionInfo.builder()
                    .httpCode(httpStatus.value())
                    .status(false)
                    .errorMessage(err)
                    .timestamp(DateUtil.getCurrentLocalDateTime())
                    .build()));
            response.setStatusCode(httpStatus);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.just(db));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Mono.empty();
        }
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token, String username) {
        Claims claims = jwtService.getAllClaimsFromToken(token);
        String rolesString = String.join(", ", claims.get("roles", ArrayList.class));
        log.info("adding below headers to request:\nusername={}\nroles={}", username, rolesString);
        exchange.getRequest().mutate()
                .header("username", username)
                .header("roles", rolesString)
                .build();
    }
}
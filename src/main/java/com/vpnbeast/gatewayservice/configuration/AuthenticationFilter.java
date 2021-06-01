package com.vpnbeast.gatewayservice.configuration;

import com.vpnbeast.gatewayservice.service.HttpService;
import com.vpnbeast.gatewayservice.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final JwtService jwtService;
    private final HttpService httpService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)) {
            if (httpService.isAuthMissing(request))
                return httpService.onError(exchange, "Authorization header is missing in request",
                        HttpStatus.UNAUTHORIZED);

            final String token = httpService.getAuthHeader(request).substring(7);
            try {
                // TODO: test token is not expired but no valid user case
                if (!jwtService.isTokenValid(token)) {
                    log.warn("no such user with provided token {}", token);
                    return httpService.onError(exchange, "No such user with provided token", HttpStatus.UNAUTHORIZED);
                }
            } catch (ExpiredJwtException exception) {
                log.warn(exception.getMessage(), exception.fillInStackTrace());
                return httpService.onError(exchange, "Given Jwt token already expired!", HttpStatus.UNAUTHORIZED);
            } catch (SignatureException exception) {
                log.warn(exception.getMessage(), exception.fillInStackTrace());
                return httpService.onError(exchange, "Unable to verify RSA signature using configured PublicKey!",
                        HttpStatus.BAD_REQUEST);
            }

            httpService.populateRequestWithHeaders(exchange, token, jwtService.getUsernameFromToken(token));
        }

        return chain.filter(exchange);
    }

}
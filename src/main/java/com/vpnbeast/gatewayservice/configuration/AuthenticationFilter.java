package com.vpnbeast.gatewayservice.configuration;

import com.vpnbeast.gatewayservice.client.AuthServiceClient;
import com.vpnbeast.gatewayservice.model.UserRequest;
import com.vpnbeast.gatewayservice.model.ValidateTokenRequest;
import com.vpnbeast.gatewayservice.model.ValidateTokenResponse;
import com.vpnbeast.gatewayservice.service.HttpService;
import com.vpnbeast.gatewayservice.service.JwtService;
import io.jsonwebtoken.Claims;
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

import java.util.Date;

@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final JwtService jwtService;
    private final HttpService httpService;
    private final AuthServiceClient authServiceClient;

    /*@Override
    public Boolean isTokenValid(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        final boolean isExpired = expiration.before(new Date());
        final UserRequest request = UserRequest.builder()
                .userName(getUsernameFromToken(token))
                .build();
        return (authServiceClient.isValidUser(request).getStatus() && !isExpired);
    }*/

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)) {
            if (httpService.isAuthMissing(request))
                return httpService.onError(exchange, "Authorization header is missing in request",
                        HttpStatus.UNAUTHORIZED);

            final String token = httpService.getAuthHeader(request).substring(7);
            final ValidateTokenResponse validateTokenResponse = authServiceClient.validateToken(ValidateTokenRequest
                    .builder()
                    .token(token)
                    .build());

            if (!validateTokenResponse.getStatus()) {
                log.warn("an error occured while validating token: {}", validateTokenResponse.getErrorMessage());
                return httpService.onError(exchange, validateTokenResponse.getErrorMessage(), HttpStatus.valueOf(validateTokenResponse.getHttpCode()));
            }

            httpService.populateRequestWithHeaders(exchange, token, jwtService.getUsernameFromToken(token));
        }

        return chain.filter(exchange);
    }

}
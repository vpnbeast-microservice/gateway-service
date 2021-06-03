package com.vpnbeast.gatewayservice.configuration;

import com.vpnbeast.gatewayservice.client.AuthServiceClient;
import com.vpnbeast.gatewayservice.exception.BusinessException;
import com.vpnbeast.gatewayservice.model.ValidateTokenRequest;
import com.vpnbeast.gatewayservice.model.ValidateTokenResponse;
import com.vpnbeast.gatewayservice.service.HttpService;
import feign.FeignException;
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
            ValidateTokenResponse validateTokenResponse;
            try {
                validateTokenResponse = authServiceClient.validateToken(ValidateTokenRequest
                        .builder()
                        .token(token)
                        .build());
            } catch (BusinessException exception) {
                // TODO: better way to handle that. when auth-service puts 401 on the response, response body is empty
                if (exception.getHttpCode() == 400)
                    return httpService.onError(exchange, exception.getErrorMessage(), HttpStatus.UNAUTHORIZED);
                return httpService.onError(exchange, exception.getErrorMessage(), HttpStatus.valueOf(exception.getHttpCode()));
            }

            httpService.populateRequestWithHeaders(exchange, validateTokenResponse.getRoles(), validateTokenResponse.getUsername());
        }

        return chain.filter(exchange);
    }

}
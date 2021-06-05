package com.vpnbeast.gatewayservice.configuration;

import com.vpnbeast.gatewayservice.client.AuthServiceClient;
import com.vpnbeast.gatewayservice.exception.ClientException;
import com.vpnbeast.gatewayservice.exception.ExceptionInfo;
import com.vpnbeast.gatewayservice.model.ValidateTokenRequest;
import com.vpnbeast.gatewayservice.model.ValidateTokenResponse;
import com.vpnbeast.gatewayservice.service.HttpService;
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
            } catch (ClientException exception) {
                // TODO: better way to handle that. when auth-service puts 401 on the response, response body is empty
                final ExceptionInfo exceptionInfo = exception.getExceptionInfo();
                if (exceptionInfo.getHttpCode() == 400)
                    return httpService.onError(exchange, exceptionInfo.getErrorMessage(), HttpStatus.UNAUTHORIZED);
                return httpService.onError(exchange, exceptionInfo.getErrorMessage(), HttpStatus.valueOf(exceptionInfo.getHttpCode()));
            }

            httpService.populateRequestWithHeaders(exchange, validateTokenResponse.getRoles(), validateTokenResponse.getUsername());
        }

        return chain.filter(exchange);
    }

}
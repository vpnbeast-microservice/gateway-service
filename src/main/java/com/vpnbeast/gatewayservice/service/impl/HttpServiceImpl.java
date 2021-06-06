package com.vpnbeast.gatewayservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpnbeast.gatewayservice.exception.ExceptionInfo;
import com.vpnbeast.gatewayservice.service.HttpService;
import com.vpnbeast.gatewayservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpServiceImpl implements HttpService {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
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
            log.error("an error occured while converting json to byte array", e);
            return Mono.empty();
        }
    }

    @Override
    public String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    @Override
    public Boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    @Override
    public void populateRequestWithHeaders(ServerWebExchange exchange, String[] roles, String username) {
        String rolesString = String.join(", ", roles);
        exchange.getRequest().mutate()
                .header("username", username)
                .header("roles", rolesString)
                .build();
    }

}

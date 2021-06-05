package com.vpnbeast.gatewayservice.controller;

import com.vpnbeast.gatewayservice.model.FallbackResponse;
import com.vpnbeast.gatewayservice.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    // check https://www.baeldung.com/spring-webflux-404 to modify response
    @GetMapping("/fallback")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<FallbackResponse> commonFallback() {
        return Mono.just(FallbackResponse.builder()
                .httpCode(500)
                .status(false)
                .errorMessage("unknown error occured at the backend")
                .timestamp(DateUtil.getCurrentLocalDateTime())
                .build());
    }

}
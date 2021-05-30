package com.vpnbeast.gatewayservice.controller;

import com.vpnbeast.gatewayservice.model.FallbackResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public Mono<FallbackResponse> commonFallback() {
        return Mono.just(FallbackResponse.builder()
                .httpCode(500)
                .status(false)
                .errorMessage("unknown error occured at the backend")
                .build());
    }

}
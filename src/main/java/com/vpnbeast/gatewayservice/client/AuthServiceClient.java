package com.vpnbeast.gatewayservice.client;

import com.vpnbeast.gatewayservice.model.ValidateTokenRequest;
import com.vpnbeast.gatewayservice.model.ValidateTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "${gateway-service.client.auth-service.url}")
public interface AuthServiceClient {

    @PostMapping(value = "/auth/validate")
    ValidateTokenResponse validateToken(@RequestBody ValidateTokenRequest validateTokenRequest);

}

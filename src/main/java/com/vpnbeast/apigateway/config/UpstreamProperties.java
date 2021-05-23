package com.vpnbeast.apigateway.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UpstreamProperties {

    @Value("${api-gateway.upstream.vpnbeast-service}")
    private String vpnbeastServiceUrl;

    @Value("${api-gateway.upstream.auth-service}")
    private String authServiceUrl;

}

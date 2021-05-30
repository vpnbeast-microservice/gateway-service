package com.vpnbeast.gatewayservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gateway-service.upstream")
public class UpstreamProperties {

    private String vpnbeastServiceUrl;
    private String authServiceUrl;

}

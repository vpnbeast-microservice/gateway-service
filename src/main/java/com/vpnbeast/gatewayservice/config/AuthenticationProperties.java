package com.vpnbeast.gatewayservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gateway-service.security")
public class AuthenticationProperties {

    private String publicKeyString;
    private String privateKeyString;

}

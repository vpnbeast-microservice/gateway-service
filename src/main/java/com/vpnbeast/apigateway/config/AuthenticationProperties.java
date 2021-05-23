package com.vpnbeast.apigateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vpnbeast-service.security")
public class AuthenticationProperties {

    private String issuer;
    private String publicKeyString;
    private String privateKeyString;
    private Long accessTokenValidInMinutes;
    private Long refreshTokenValidInMinutes;
    private Long verificationCodeValidInMinutes;

}

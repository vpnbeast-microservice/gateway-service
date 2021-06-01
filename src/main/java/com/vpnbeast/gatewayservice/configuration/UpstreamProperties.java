package com.vpnbeast.gatewayservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Getter
@Setter
@Configuration
public class UpstreamProperties {

    // Vpnbeast service related stuff
    @Value("${gateway-service.upstream.vpnbeast-service.url}")
    private String vpnbeastServiceUrl;
    @Value("${gateway-service.upstream.vpnbeast-service.uris}")
    private String[] vpnbeastServiceUris;

    // Auth service related stuff
    @Value("${gateway-service.upstream.auth-service.url}")
    private String authServiceUrl;
    @Value("${gateway-service.upstream.auth-service.uris}")
    private String[] authServiceUris;

}

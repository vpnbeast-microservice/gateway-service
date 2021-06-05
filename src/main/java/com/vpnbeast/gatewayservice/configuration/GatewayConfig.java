package com.vpnbeast.gatewayservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter filter;
    private final UpstreamProperties upstreamProperties;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        // TODO: looks like fallback not running
        // "/users/**", "/servers/**", "/admin/**"

        return builder.routes()
                .route("vpnbeast-service", r -> r.path(upstreamProperties.getVpnbeastServiceUris())
                        .filters(f -> f.hystrix(config -> config
                                .setName("fallback1")
                                .setFallbackUri("forward:/fallback")).filter(filter))
                        .uri(upstreamProperties.getVpnbeastServiceUrl()))

                .route("auth-service", r -> r.path(upstreamProperties.getAuthServiceUris())
                        .filters(f -> f.hystrix(config -> config
                                .setName("fallback2")
                                .setFallbackUri("forward:/fallback")).filter(filter))
                        .uri(upstreamProperties.getAuthServiceUrl()))
                .build();
    }

}
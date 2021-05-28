package com.vpnbeast.gatewayservice.config;

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
        return builder.routes()
                .route("vpnbeast-service", r -> r.path("/users/**", "/servers/**", "/admin/**")
                        .filters(f -> f.filter(filter))
                        .uri(upstreamProperties.getVpnbeastServiceUrl()))

                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri(upstreamProperties.getAuthServiceUrl()))
                .build();
    }

}
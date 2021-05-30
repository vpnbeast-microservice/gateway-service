package com.vpnbeast.gatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "gateway-service.upstream.vpnbeastServiceUrl=http://localhost:9090",
        "gateway-service.upstream.authServiceUrl=http://localhost:5000"})
class GatewayServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

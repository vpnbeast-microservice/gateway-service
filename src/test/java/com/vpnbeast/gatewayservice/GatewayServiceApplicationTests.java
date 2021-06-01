package com.vpnbeast.gatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// TODO: better way to handle test properties
@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "gateway-service.client.auth-service.listOfServers=http://localhost:5000",
        "gateway-service.upstream.vpnbeastServiceUrl=http://localhost:9090",
        "gateway-service.upstream.authServiceUrl=http://localhost:5000"})
class GatewayServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

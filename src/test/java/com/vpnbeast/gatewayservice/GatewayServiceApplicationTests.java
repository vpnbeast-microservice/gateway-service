package com.vpnbeast.gatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// TODO: better way to handle test properties
@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "gateway-service.client.auth-service.url=http://localhost:5000",
        "gateway-service.upstream.vpnbeast-service.url=http://localhost:9090",
        "gateway-service.upstream.vpnbeast-service.uris='/users/**,/servers/**,/admin/**'",
        "gateway-service.upstream.auth-service.url=http://localhost:5000",
        "gateway-service.upstream.auth-service.uris='/auth/**'"})
class GatewayServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

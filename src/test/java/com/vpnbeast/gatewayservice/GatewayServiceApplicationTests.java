package com.vpnbeast.gatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// @ActiveProfiles("unit-test")
@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
class GatewayServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

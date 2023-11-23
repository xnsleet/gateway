package io.sleet.gateway;

import io.sleet.gateway.router.WeightHttpEndPointRouter;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class GatewayApplicationTests {

    @Resource
    WeightHttpEndPointRouter weightHttpEndPointRouter;

    @Test
    void contextLoads() {
        List<String> list = Arrays.asList(
                "http://localhost:8801,http://localhost:8802,http://localhost:8803".split(","));
        weightHttpEndPointRouter.router(list);
    }

}

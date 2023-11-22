package io.sleet.url;

import io.sleet.gateway.router.WeightHttpEndPointRouter;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author sleet
 * @date 2023年11月22日 18:10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UrlTest {

    @Resource
    WeightHttpEndPointRouter weightHttpEndPointRouter;

    @Test
    public void weight(){
        List<String> list = Arrays.asList("http://localhost:8801,http://localhost:8802,http://localhost:8803".split(","));
        weightHttpEndPointRouter.router(list);
    }
}

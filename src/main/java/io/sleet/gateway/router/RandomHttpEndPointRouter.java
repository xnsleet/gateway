package io.sleet.gateway.router;

import io.sleet.gateway.constans.RouterConstants;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * 随机路由
 */
@Component
public class RandomHttpEndPointRouter
        implements HttpEndPointRouterStrategy {

    @Override
    public String type() {
        return RouterConstants.RANDOM;
    }

    @Override
    public String router(List<String> urls) {
        int size = urls.size();
        Random random = new Random(System.currentTimeMillis());
        return urls.get(random.nextInt(size));
    }
}

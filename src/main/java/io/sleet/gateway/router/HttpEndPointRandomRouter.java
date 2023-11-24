package io.sleet.gateway.router;

import io.sleet.gateway.constans.RouterConstants;
import io.sleet.gateway.router.strategy.HttpEndPointRouterStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @description 随机路由
 * @author sleet
 */
@Component
public class HttpEndPointRandomRouter
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

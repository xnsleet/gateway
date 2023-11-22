package io.sleet.gateway.router;

import java.util.List;
import java.util.Random;

/**
 * 随机路由
 */
public class RandomHttpEndPointRouter
        implements HttpEndPointRouter {
    @Override
    public String router(List<String> urls) {
        int size = urls.size();
        Random random = new Random(System.currentTimeMillis());
        return urls.get(random.nextInt(size));
    }
}

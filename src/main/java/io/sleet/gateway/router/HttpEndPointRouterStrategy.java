package io.sleet.gateway.router;

import java.util.List;

/**
 * @author sleet
 * @date 2023年11月23日 15:57
 */
public interface HttpEndPointRouterStrategy {

    String type();

    String router(List<String> urls);
}

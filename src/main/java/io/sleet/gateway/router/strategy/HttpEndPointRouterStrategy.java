package io.sleet.gateway.router.strategy;

import java.util.List;

public interface HttpEndPointRouterStrategy {

    String type();

    String router(List<String> urls);
}

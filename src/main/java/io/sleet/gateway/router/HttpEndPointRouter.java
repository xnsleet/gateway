package io.sleet.gateway.router;

import java.util.List;

public interface HttpEndPointRouter {
    String router(List<String> urls);
}

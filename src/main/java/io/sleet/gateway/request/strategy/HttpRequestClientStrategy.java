package io.sleet.gateway.request.strategy;

import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.http.client.methods.HttpRequestBase;

public interface HttpRequestClientStrategy {

    String type();

    HttpRequestBase getClient(FullHttpRequest request);
}

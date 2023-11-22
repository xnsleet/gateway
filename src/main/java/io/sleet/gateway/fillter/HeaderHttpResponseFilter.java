package io.sleet.gateway.fillter;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 响应过滤器
 */
public class HeaderHttpResponseFilter
        implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        System.out.println("response sleet");
    }
}

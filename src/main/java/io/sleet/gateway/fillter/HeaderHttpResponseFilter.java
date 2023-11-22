package io.sleet.gateway.fillter;

import io.netty.handler.codec.http.FullHttpResponse;

public class HeaderHttpResponseFilter
        implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        System.out.println("response sleet");
    }
}

package io.sleet.gateway.fillter;

import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @description 响应过滤器
 * @author sleet
 */
@Component
public class HeaderHttpResponseFilter
        implements HttpResponseFilter {

    @Override
    public void filter(FullHttpResponse response) {
        System.out.println("response sleet");
    }
}

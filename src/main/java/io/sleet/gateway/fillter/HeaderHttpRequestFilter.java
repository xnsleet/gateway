package io.sleet.gateway.fillter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.stereotype.Component;

/**
 * @description 请求过滤器
 * @author sleet
 */
@Component
public class HeaderHttpRequestFilter
        implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest request, ChannelHandlerContext channelHandlerContext) {
        System.out.println("request sleet");
    }
}

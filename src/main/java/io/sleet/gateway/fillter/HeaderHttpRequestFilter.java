package io.sleet.gateway.fillter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.stereotype.Component;

/**
 * @author sleet
 * @description 请求过滤器
 */
@Component
public class HeaderHttpRequestFilter
        implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest request, ChannelHandlerContext channelHandlerContext) {
        System.out.println("request sleet");
    }
}

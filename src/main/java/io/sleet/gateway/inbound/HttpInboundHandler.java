package io.sleet.gateway.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import io.sleet.gateway.fillter.HeaderHttpRequestFilter;
import io.sleet.gateway.outbound.HttpOutboundHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 请求入站处理器
 */
@Component
public class HttpInboundHandler
        extends ChannelInboundHandlerAdapter {
重构
    @Resource
    private HttpOutboundHandler outboundHandler;

    @Resource
    private HeaderHttpRequestFilter requestFilter;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            outboundHandler.handler(fullHttpRequest,ctx,requestFilter);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

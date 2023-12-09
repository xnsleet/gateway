package io.sleet.gateway.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import io.sleet.gateway.outbound.HttpOutboundHandler;

/**
 * @author sleet
 * @description 请求入站处理器
 */
public class HttpInboundHandler
        extends ChannelInboundHandlerAdapter {

    private final HttpOutboundHandler outboundHandler;

    public HttpInboundHandler(HttpOutboundHandler outboundHandler) {
        this.outboundHandler = outboundHandler;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            outboundHandler.handler((FullHttpRequest) msg, ctx);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

package io.sleet.gateway.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import io.sleet.gateway.fillter.HeaderHttpRequestFilter;
import io.sleet.gateway.outbound.HttpOutboundHandler;

import java.util.List;

public class HttpInboundHandler
        extends ChannelInboundHandlerAdapter {

    private List<String> proxyServer;

    private HttpOutboundHandler outboundHandler;

    private HeaderHttpRequestFilter requestFilter = new HeaderHttpRequestFilter();


    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
        this.outboundHandler = new HttpOutboundHandler(this.proxyServer);
    }

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

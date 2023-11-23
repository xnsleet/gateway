package io.sleet.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.sleet.gateway.outbound.HttpOutboundHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.lang.ref.PhantomReference;

/**
 * 请求入站初始化
 */
@Component
public class HttpInboundInitializer
        extends ChannelInitializer<SocketChannel> {

    @Resource
    private HttpOutboundHandler httpOutboundHandler;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new HttpInboundHandler(httpOutboundHandler));
    }
}

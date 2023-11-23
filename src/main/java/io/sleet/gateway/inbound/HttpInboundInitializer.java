package io.sleet.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 请求入站初始化
 */
@Component
public class HttpInboundInitializer
        extends ChannelInitializer<SocketChannel> {

    @Resource
    private HttpInboundHandler httpInboundHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(httpInboundHandler);
    }
}

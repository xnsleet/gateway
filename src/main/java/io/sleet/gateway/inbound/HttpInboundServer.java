package io.sleet.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 服务启动器
 */
@Component
public class HttpInboundServer{

    @Value("${netty.group.boss}")
    private int bossGroupNum;

    @Value("${netty.group.worker}")
    private int workerGroupNum;

    @Value("${server.port}")
    private int proxyPort;

    @Value("${proxy.servers}")
    private String proxyServers;

    @PostConstruct
    public void init(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(bossGroupNum);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(workerGroupNum);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new HttpInboundInitializer(Arrays.asList(proxyServers.split(","))));

            Channel channel = bootstrap.bind(proxyPort).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

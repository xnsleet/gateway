package io.sleet.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.List;

public class HttpInboundServer {

    private List<String> proxyServer;

    private int port;

    public HttpInboundServer(int port, List<String> proxyServer) {
        this.port = port;
        this.proxyServer = proxyServer;
    }

    public void run() throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(16);

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
                    .childHandler(new HttpInboundInitializer(proxyServer));

            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

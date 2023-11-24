package io.sleet.gateway.client;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.springframework.stereotype.Component;

/**
 * @author sleet
 * @date 2023-11-24 14:35
 * @description 异步客户端
 */
@Component
public class HttpAsyncClient {

    private CloseableHttpAsyncClient httpAsyncClient;

    public HttpAsyncClient(){
        IOReactorConfig reactorConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setRcvBufSize(32 * 1024)
                .build();

        this.httpAsyncClient = HttpAsyncClients.custom()
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(reactorConfig)
                .setKeepAliveStrategy((response, context) -> 6000)
                .build();

        this.httpAsyncClient.start();
    }

    public CloseableHttpAsyncClient initializer(){
        return this.httpAsyncClient;
    }
}

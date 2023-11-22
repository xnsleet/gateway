package io.sleet.gateway.outbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.sleet.gateway.fillter.HeaderHttpResponseFilter;
import io.sleet.gateway.fillter.HttpRequestFilter;
import io.sleet.gateway.router.RandomHttpEndPointRouter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;

public class HttpOutboundHandler {

    private CloseableHttpAsyncClient httpAsyncClient;

    private List<String> backedUrls;

    private ExecutorService proxyService;

    private HeaderHttpResponseFilter responseFilter = new HeaderHttpResponseFilter();

    private RandomHttpEndPointRouter randomHttpEndPointRouter = new RandomHttpEndPointRouter();

    public HttpOutboundHandler(List<String> backedUrls) {
        this.backedUrls = backedUrls;

        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 1024;
        proxyService = new ThreadPoolExecutor(
                cores, queueSize, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize));

        IOReactorConfig reactorConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(cores)
                .setRcvBufSize(32 * 1024)
                .build();

        httpAsyncClient = HttpAsyncClients.custom()
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(reactorConfig)
                .setKeepAliveStrategy((response, context) -> 6000)
                .build();
        httpAsyncClient.start();
    }

    public void handler(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter requestFilter) {
        String backedUrl = randomHttpEndPointRouter.router(this.backedUrls);
        String url = backedUrl + fullRequest.getUri();
        requestFilter.filter(fullRequest, ctx);
        proxyService.submit(() -> fetchGet(fullRequest, ctx, url));
    }

    private void fetchGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        httpGet.setHeader("name", "sleet");
        httpAsyncClient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse response) {
                try {
                    handleResponse(fullRequest, ctx, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception ex) {
                httpGet.abort();
                ex.printStackTrace();
            }

            @Override
            public void cancelled() {
                httpGet.abort();
            }
        });
    }

    private void handleResponse(final FullHttpRequest fullRequest,final  ChannelHandlerContext ctx,final  HttpResponse endpointResponse) {
        FullHttpResponse fullResponse = null;
        try {
            byte[] byteArray = EntityUtils.toByteArray(endpointResponse.getEntity());
            fullResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(byteArray));
            fullResponse.headers().set("Content-Type", "application/json");
            fullResponse.headers().set("Content-Length", Integer.parseInt(endpointResponse.getFirstHeader("Content-Length").getValue()));
            responseFilter.filter(fullResponse);
        } catch (Exception e) {
            fullResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, NO_CONTENT);
            ctx.close();
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(fullResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(fullResponse);
                }
            }
            ctx.flush();
        }
    }
}
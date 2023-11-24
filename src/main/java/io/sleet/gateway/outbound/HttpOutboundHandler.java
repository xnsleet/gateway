package io.sleet.gateway.outbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.sleet.gateway.client.HttpAsyncClient;
import io.sleet.gateway.config.AppConfiguration;
import io.sleet.gateway.fillter.HeaderHttpRequestFilter;
import io.sleet.gateway.fillter.HeaderHttpResponseFilter;
import io.sleet.gateway.router.strategy.HttpEndPointRouterChoose;
import io.sleet.gateway.router.strategy.HttpEndPointRouterStrategy;
import io.sleet.gateway.thread.HttpThreadPoolExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;

/**
 * @author sleet
 * @description 请求出站处理器
 */
@Component
@Slf4j
public class HttpOutboundHandler {

    @Resource
    private HttpAsyncClient httpAsyncClient;

    @Resource
    private AppConfiguration appConfiguration;

    @Resource
    private HeaderHttpRequestFilter requestFilter;

    @Resource
    private HeaderHttpResponseFilter responseFilter;

    @Resource
    private HttpEndPointRouterChoose httpEndPointRouterChoose;

    public HttpOutboundHandler() {
    }

    public void handler(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        List<String> urlList = Arrays.asList(appConfiguration.proxyServers.split(","));
        HttpEndPointRouterStrategy httpEndPointRouterStrategy = httpEndPointRouterChoose.choose(appConfiguration.routerChooseType);
        String backedUrl = httpEndPointRouterStrategy.router(urlList);
        String url = backedUrl + fullRequest.uri();
        requestFilter.filter(fullRequest, ctx);
        HttpThreadPoolExecutor.executor().submit(() -> fetchGet(fullRequest, ctx, url));
    }

    private void fetchGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        httpGet.setHeader("name", "sleet");
        httpAsyncClient.initializer().execute(httpGet, new FutureCallback<HttpResponse>() {
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

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) {
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

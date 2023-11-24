package io.sleet.gateway.request;

import io.netty.handler.codec.http.FullHttpRequest;
import io.sleet.gateway.constans.RequestConstants;
import io.sleet.gateway.request.strategy.HttpRequestClientStrategy;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Component;

/**
 * @author sleet
 * @date 2023-11-24 17:49
 * @description GET请求客户端
 */
@Component
public class HttpGetClient
        implements HttpRequestClientStrategy {

    @Override
    public String type() {
        return RequestConstants.GET;
    }

    @Override
    public HttpRequestBase getClient(FullHttpRequest request) {
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        return httpGet;
    }
}

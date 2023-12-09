package io.sleet.gateway.request;

import io.netty.handler.codec.http.FullHttpRequest;
import io.sleet.gateway.constans.RequestConstants;
import io.sleet.gateway.request.strategy.HttpRequestClientStrategy;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Component;

/**
 * @author sleet
 * @date 2023-11-24 17:50
 * @description POST请求客户端
 */
@Component
public class HttpPostClient
        implements HttpRequestClientStrategy {

    @Override
    public String type() {
        return RequestConstants.POST;
    }

    @Override
    public HttpRequestBase getClient(FullHttpRequest request, String url) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        return httpPost;
    }
}

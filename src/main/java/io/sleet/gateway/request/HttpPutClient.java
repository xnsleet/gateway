package io.sleet.gateway.request;

import io.netty.handler.codec.http.FullHttpRequest;
import io.sleet.gateway.constans.RequestConstants;
import io.sleet.gateway.request.strategy.HttpRequestClientStrategy;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Component;

/**
 * @author sleet
 * @date 2023-11-26 15:49
 * @description PUT请求客户端
 */
@Component
public class HttpPutClient
        implements HttpRequestClientStrategy {

    @Override
    public String type() {
        return RequestConstants.PUT;
    }

    @Override
    public HttpRequestBase getClient(FullHttpRequest request, String url) {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        return httpPut;
    }
}

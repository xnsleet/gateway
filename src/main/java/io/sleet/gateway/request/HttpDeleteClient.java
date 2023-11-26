package io.sleet.gateway.request;

import io.netty.handler.codec.http.FullHttpRequest;
import io.sleet.gateway.constans.RequestConstants;
import io.sleet.gateway.request.strategy.HttpRequestClientStrategy;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Component;

/**
 * @author sleet
 * @date 2023-11-26 15:53
 * @description DELETE请求客户端
 */
@Component
public class HttpDeleteClient
        implements HttpRequestClientStrategy {
    @Override
    public String type() {
        return RequestConstants.DELETE;
    }

    @Override
    public HttpRequestBase getClient(FullHttpRequest request, String url) {
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        return httpDelete;
    }
}

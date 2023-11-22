package io.sleet.gateway;

import io.sleet.gateway.inbound.HttpInboundServer;

import java.util.Arrays;

public class GatewayApplication {
    public static void main(String[] args) throws InterruptedException {
        String proxyPort = System.getProperty("proxyPort", "9999");
        String proxyServers = System.getProperty("proxyServers",
                "http://localhost:8801,http://localhost:8802,http://localhost:8803");
        int port = Integer.parseInt(proxyPort);
        HttpInboundServer server = new HttpInboundServer(port, Arrays.asList(proxyServers.split(",")));
        server.run();
    }
}

package io.sleet.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description 权重路由配置
 * @author sleet
 * @date 2023-11-24 10:28
 */
@Component
public class AppConfiguration {

    @Value("${server.port}")
    public int serverPort;

    @Value("${proxy.servers}")
    public String proxyServers;

    @Value("${netty.group.boss}")
    public int nettyGroupBoss;

    @Value("${netty.group.worker}")
    public int nettyGroupWorker;

    @Value("${router.choose.type}")
    public String routerChooseType;

    @Value("${router.weight.proportion}")
    public String routerWeightProportion;

    @Value("${router.weight.switch}")
    public Boolean routerWeightSwitch;
}

package io.sleet.gateway.router.strategy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sleet
 * @description 路由策略
 */
@Component
public class HttpEndPointRouterChoose
        implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Map<String, HttpEndPointRouterStrategy> strategyMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, HttpEndPointRouterStrategy> beansOfType = applicationContext.getBeansOfType(HttpEndPointRouterStrategy.class);
        beansOfType.forEach((key,bean)->{
            this.strategyMap.put(bean.type(),bean);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public HttpEndPointRouterStrategy choose(String name) {
        return this.strategyMap.get(name);
    }
}

package io.sleet.gateway.router.strategy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sleet
 * @date 2023年11月23日 16:01
 */
@Component
public class HttpEndPointRouterChoose
        implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Map<String, HttpEndPointRouterStrategy> map;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.map = applicationContext.getBeansOfType(HttpEndPointRouterStrategy.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public HttpEndPointRouterStrategy choose(String name) {
        return this.map.get(name);
    }
}

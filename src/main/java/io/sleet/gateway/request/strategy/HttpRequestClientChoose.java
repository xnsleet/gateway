package io.sleet.gateway.request.strategy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sleet
 * @date 2023-11-24 17:42
 * @description 请求客户端策略
 */
@Component
public class HttpRequestClientChoose
        implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private HashMap<String, HttpRequestClientStrategy> strategyMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, HttpRequestClientStrategy> beansOfType =
                this.applicationContext.getBeansOfType(HttpRequestClientStrategy.class);
        beansOfType.forEach((type, bean) -> {
            this.strategyMap.put(bean.type(), bean);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public HttpRequestClientStrategy choose(String name) {
        return this.strategyMap.get(name);
    }
}

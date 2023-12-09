package io.sleet.gateway.router;

import io.sleet.gateway.constans.RouterConstants;
import io.sleet.gateway.router.strategy.HttpEndPointRouterStrategy;
import io.sleet.gateway.config.AppConfiguration;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author sleet
 * @description 权重路由
 */
@Component
@RequiredArgsConstructor
public class HttpEndPointWeightRouter
        implements HttpEndPointRouterStrategy {

    private final AppConfiguration appConfiguration;

    @Override
    public String type() {
        return RouterConstants.WEIGHT;
    }

    @Override
    public String router(List<String> urls) {
        if (StringUtils.isBlank(appConfiguration.routerWeightProportion)) {
            throw new RuntimeException("No weighting rules defined");
        }
        if (!Boolean.TRUE.equals(appConfiguration.routerWeightSwitch)) {
            throw new RuntimeException("Weight switch not opened");
        }

        String[] splitWeight = appConfiguration.routerWeightProportion.split(",");
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < splitWeight.length; i++) {
            for (int j = 0; j < Integer.parseInt(splitWeight[i]); j++) {
                urlList.add(urls.get(i));
            }
        }

        int randomNum = new Random().nextInt(urlList.size());
        return urlList.get(randomNum);
    }
}

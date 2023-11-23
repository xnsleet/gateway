package io.sleet.gateway.router;

import io.sleet.gateway.constans.RouterConstants;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * 权重路由
 */
@Component
public class WeightHttpEndPointRouter
        implements HttpEndPointRouterStrategy {

    @Override
    public String type() {
        return RouterConstants.WEIGHT;
    }

    @Override
    public String router(List<String> urls) {
        YamlPropertiesFactoryBean yamlProFb = new YamlPropertiesFactoryBean();
        yamlProFb.setResources(new ClassPathResource("application.yml"));
        Properties properties = yamlProFb.getObject();
        assert properties != null;
        String weight = (String) properties.get("proxy.weight");
        String[] splitWeight = weight.split(",");
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < splitWeight.length; i++) {
            for (int j = 0; j < Integer.parseInt(splitWeight[i]); j++) {
                urlList.add(urls.get(i));
            }
        }
        Random random = new Random();
        int i = random.nextInt(urlList.size());
        System.out.println(urlList.get(i));
        return urlList.get(i);
    }
}

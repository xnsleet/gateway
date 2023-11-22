package io.sleet.gateway.router;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 权重路由
 */
@Component
public class WeightHttpEndPointRouter
        implements HttpEndPointRouter {

    @Value("${proxy.weight}")
    private String weight;

    @Override
    public String router(List<String> urls) {
        String[] splitWeight = weight.split(",");
        HashMap<String, String> map = new HashMap<>(urls.size());
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < splitWeight.length; i++) {
            for (int j = 0; j < Integer.parseInt(splitWeight[i]); j++) {
                urlList.add(urls.get(i));
            }
        }
        Random random = new Random();
        int i = random.nextInt(urlList.size());
        return urlList.get(i);
    }
}

package io.sleet.gateway.thread;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sleet
 * @date 2023-11-24 14:14
 * @description 线程池执行器
 */
@Component
public class HttpThreadPoolExecutor {

    private static final int corePoolSize = Runtime.getRuntime().availableProcessors();

    private static final int maximumPoolSize = 20;

    private static final int keepAliveTime = 1000;

    private static final int queueSize = 1024;

    private static final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                    TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize));

    public static ExecutorService executor(){
        return executor;
    }
}

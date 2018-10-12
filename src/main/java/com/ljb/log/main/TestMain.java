package com.ljb.log.main;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ljb.log.LogConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.*;

/**
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("log-%d").build();
        ExecutorService executorService  = new ThreadPoolExecutor(10000,10000,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(10240),threadFactory);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LogConfig.class);
    }
}

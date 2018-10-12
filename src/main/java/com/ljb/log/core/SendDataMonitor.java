package com.ljb.log.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.concurrent.*;

/**
 * 发送数据监控者
 * 里面维护了一个队列，以缓存即将要发送的日志数据
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public class SendDataMonitor {

    private JmsTemplate jmsTemplate;
    /**
     * 线程工厂
     */
    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("log-%d").build();
    /**
     * 线程池
     */
    private ExecutorService executorService  = new ThreadPoolExecutor(1,1,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(10),threadFactory);
    /**
     * 用来存储即将发送的数据
     */
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    /**
     * 最大缓存的消息数量
     */
    private Integer maxCacheCount = 1000000;

    public Integer getMaxCacheCount() {
        return maxCacheCount;
    }

    public void setMaxCacheCount(Integer maxCacheCount) {
        this.maxCacheCount = maxCacheCount;
    }

    /**
     * 初始化方法
     * 开启一个线程发送队列里面的内容到MQ中
     */
    public void init(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
        executorService.execute(new Runnable() {
            public void run() {
                runSend();
            }
        });
    }

    /**
     * 循环从队列中取消息并发送到Mq中
     */
    private void runSend(){
        while (true){
            try {
                final String message = this.queue.take();
                System.out.println("send : "+message);
                if(jmsTemplate!=null){
                    jmsTemplate.send("log", new MessageCreator() {
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(message);
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  添加需要发送的消息
     * @param message
     * @throws InterruptedException
     */
    public void addSendMessage(String message){
        //消息存储最大值
        try {
            if(this.queue.size() <= maxCacheCount){
                this.queue.put(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

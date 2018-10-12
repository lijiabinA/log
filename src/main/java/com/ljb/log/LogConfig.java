package com.ljb.log;

import com.ljb.log.core.AbstractLogAdapter;
import com.ljb.log.core.LogOfActiveMqAdapter;
import com.ljb.log.core.SendDataMonitor;
import com.ljb.log.service.SendService;
import com.ljb.log.service.impl.ActiveMqSendJsonServiceImpl;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ResourceUtils;

import javax.jms.ConnectionFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.ljb.log")
public class LogConfig {

    private Logger logger = Logger.getLogger(LogConfig.class);


    @Bean
    @Qualifier("myProperties")
    public Properties initProperties() throws IOException {
        try {
            File file = ResourceUtils.getFile("classpath:log.properties");
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            return properties;
        } catch (FileNotFoundException e) {
            logger.error("在classpath中未找到log.properties文件，请创建！！！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        properties.put("error","yes");
        return properties;
    }


    @Bean
    public AbstractLogAdapter getLogAdapter(@Qualifier("myProperties")Properties properties) {
        String activeMqAddress = properties.getProperty("activeMqAddress");
        String error = properties.getProperty("error");
        if(error!=null && properties.size()==0){
            logger.warn("配置文件中没有配置任何信息："+properties);
        }else if(error==null){
            logger.info("加载文件成功：" + properties);
        }
        if (activeMqAddress == null) {
            activeMqAddress = "";
            logger.error("无法使用ActiveMq，在log.properties中没有指定activeMqAddress的值");
        }
        return new LogOfActiveMqAdapter(activeMqAddress);
    }

    /**
     * 实例化ActiveMq的连接池
     *
     * @param mqAdapter
     * @return
     */
    @Bean
    public PooledConnectionFactory getPooledConnectionFactory(AbstractLogAdapter mqAdapter) {
        PooledConnectionFactory factory = new PooledConnectionFactory();
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(mqAdapter.getMqAddress());
        activeMQConnectionFactory.setSendAcksAsync(true);
        activeMQConnectionFactory.setUseAsyncSend(true);
        activeMQConnectionFactory.setAlwaysSyncSend(false);
        activeMQConnectionFactory.setSendTimeout(500);
        factory.setConnectionFactory(activeMQConnectionFactory);
        return factory;
    }

    /**
     * 实例化SendDataMonitor
     * 发送数据的监视器
     *
     * @param pooledConnectionFactory
     * @return
     */
    @Bean
    public SendDataMonitor getSendDataMonitor(ConnectionFactory pooledConnectionFactory,@Qualifier("myProperties")Properties properties) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(pooledConnectionFactory);
        SendDataMonitor sendDataMonitor = new SendDataMonitor();
        String maxCacheCountStr = properties.getProperty("maxCacheCount");
        if(maxCacheCountStr!=null){
            try {
                int maxCacheCount = Integer.parseInt(maxCacheCountStr);
                sendDataMonitor.setMaxCacheCount(maxCacheCount);
            }catch (Exception e){
                logger.error("最大的缓存数量maxCacheCount解析有误，请使用int值");
            }
        }
        sendDataMonitor.init(jmsTemplate);
        return sendDataMonitor;
    }

    @Bean
    public SendService initActiveMqSendJsonServiceImpl() {
        return new ActiveMqSendJsonServiceImpl();
    }
}

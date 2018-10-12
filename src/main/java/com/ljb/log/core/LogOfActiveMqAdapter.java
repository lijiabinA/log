package com.ljb.log.core;

import com.ljb.log.core.AbstractLogAdapter;

/**
 * 日志适配器
 * 当Spring容器启动时，此方法应该初始化
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public class LogOfActiveMqAdapter extends AbstractLogAdapter {

    /**
     * ActiveMq的地址
     */
    private String activeMqAddress;
    /**
     * 指定ActiveMq的地址
     * @param activeMqAddress kafka的地址
     */
    public LogOfActiveMqAdapter(String activeMqAddress){
        this.activeMqAddress = activeMqAddress;
    }

    @Override
    public String getMqAddress() {
        return activeMqAddress;
    }
}

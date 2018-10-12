package com.ljb.log.service.impl;

import com.google.gson.Gson;
import com.ljb.log.core.SendDataMonitor;
import com.ljb.log.domain.BaseLog;
import com.ljb.log.service.SendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public class ActiveMqSendJsonServiceImpl implements SendService {

    @Resource
    private SendDataMonitor sendDataMonitor;

    private Gson gson = new Gson();

    public void send(BaseLog baseLog) {
        final String jsonMessage = gson.toJson(baseLog);
        sendDataMonitor.addSendMessage(jsonMessage);
    }
}

package com.ljb.log.service;

import com.ljb.log.domain.BaseLog;

/**
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public interface SendService {

    /**
     * 发送消息
     * @param baseLog 要发送的日志信息
     */
    void send(BaseLog baseLog);

}

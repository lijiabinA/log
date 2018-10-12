package com.ljb.log.core;

/**
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public abstract class AbstractLogAdapter implements LogAdapter {
    /**
     * 获取消息队列的地址
     * @return
     */
    public abstract String getMqAddress();
}

package com.ljb.log.domain;

/**
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public class BaseLog {
    /**
     * 日志类型
     */
    private String logType;
    /**
     * 日志时间
     */
    private Long logTime;

    public BaseLog(){
        this.logTime = System.currentTimeMillis();
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }
}

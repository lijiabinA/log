package com.ljb.log.domain;

/**
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public class ErrorLog extends BaseLog {
    /**
     * 错误发生的类
     */
    private String errorClassName;
    /**
     * 错误发生的方法
     */
    private String errorMethodName;
    /**
     * 错误消息
     */
    private String errorMessage;

    public ErrorLog(){
        this.setLogType("error");
    }

    public String getErrorClassName() {
        return errorClassName;
    }

    public void setErrorClassName(String errorClassName) {
        this.errorClassName = errorClassName;
    }

    public String getErrorMethodName() {
        return errorMethodName;
    }

    public void setErrorMethodName(String errorMethodName) {
        this.errorMethodName = errorMethodName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

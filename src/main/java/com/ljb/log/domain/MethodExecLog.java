package com.ljb.log.domain;

import java.util.List;

/**
 * 方法日志
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
public class MethodExecLog extends BaseLog {

    public MethodExecLog(){
        this.setLogType("methodExec");
    }

    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 返回类型名称
     */
    private String returnTypeName;
    /**
     * 参数类型列表
     */
    private List<String> parameterTypesList;
    /**
     * 执行所用时间
     */
    private Long executeCurrent;


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public void setReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
    }

    public List<String> getParameterTypesList() {
        return parameterTypesList;
    }

    public void setParameterTypesList(List<String> parameterTypesList) {
        this.parameterTypesList = parameterTypesList;
    }

    public Long getExecuteCurrent() {
        return executeCurrent;
    }

    public void setExecuteCurrent(Long executeCurrent) {
        this.executeCurrent = executeCurrent;
    }
}

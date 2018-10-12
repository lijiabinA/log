package com.ljb.log.aspect;

import com.ljb.log.annotation.MethodLog;
import com.ljb.log.domain.ErrorLog;
import com.ljb.log.domain.MethodExecLog;
import com.ljb.log.service.SendService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 日志拦截切面
 *
 * @author 李嘉宾
 * @version 1.0
 * @date 2018年10月11
 */
@Component
@Aspect
public class MethodLogAspect {
    @Resource
    private SendService sendService;

    @Around("within(@com.ljb.log.annotation.MethodLog *) || @annotation(com.ljb.log.annotation.MethodLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> classObj = joinPoint.getTarget().getClass();
        //判断类中是否存在MethodLod注解
        if (classObj.getAnnotation(MethodLog.class) != null) {
            //说明其中的方法都要统计
            return logCommit(joinPoint);
        } else {
            //在方法中是否发现注解
            if (method.getAnnotation(MethodLog.class) != null) {
                //该方法需要记录日志
                return logCommit(joinPoint);
            }
        }
        return joinPoint.proceed();
    }

    private Object logCommit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodExecLog methodExecLog = new MethodExecLog();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        try {
            //方法名称
            String methodName = method.getName();
            methodExecLog.setMethodName(methodName);
            //方法执行前时间戳
            long executePreTime = System.currentTimeMillis();
            //返回值类型
            String returnName = method.getReturnType().getName();
            methodExecLog.setReturnTypeName(returnName);
            List<String> parameterTypesList = new ArrayList<String>();
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class c : parameterTypes) {
                parameterTypesList.add(c.getName());
            }
            methodExecLog.setParameterTypesList(parameterTypesList);
            //执行方法
            Object proceed = joinPoint.proceed();
            //方法执行后时间戳
            long executeAfterTime = System.currentTimeMillis();
            //方法执行的时间
            long executeCurrent = executeAfterTime - executePreTime;
            methodExecLog.setExecuteCurrent(executeCurrent);
            sendService.send(methodExecLog);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //发送错误日志
            String message = throwable.getMessage();
            ErrorLog errorLog = new ErrorLog();
            errorLog.setErrorClassName(joinPoint.getTarget().getClass().getName());
            errorLog.setErrorMethodName(method.getName());
            errorLog.setErrorMessage(message);
            sendService.send(errorLog);
        }
        return joinPoint.proceed();
    }
}

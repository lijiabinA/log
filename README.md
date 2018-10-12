# log
一个通用的基于Spring Aop实现的web后端日志收集模块
## 依赖
1. Spring aop
2. log4j
3. activeMq

## 使用步骤

1. 可以直接在项目中将源码引入或直接引入已经打包好的jar包。
2. 在classpath下新建log.properties文件
3. 在log.properties文件中配置如下内容
```
# ActiveMq的访问地址
activeMqAddress=tcp://172.16.30.28:61616
# 内存中缓存的日志最大数量
maxCacheCount=1000000
```
4. 如果你使用的是SpringBoot,使用@Import注解将本项目中的LogConfig配置类引入即可。如果是普通的Spring项目,需要在配置文件中使用<context:component-scan>扫描到LogConfig配置类；
5. 在想要记录日志的类或方法中加入@MethodLog注解；
6. 此时，如果你的ActiveMq访问路径配置没有问题，应该就能够发送日志了（日志采用json格式发送）。
## 特性

1. 使用异步队列的方式发送日志，不会影响原始方法的访问。
2. 采用aop方式，对代码没有侵入性。
3. 采用Mq的方式，后续可以对日志进行统计和分析。

## 目前日志采集的内容

### 方法执行日志
1. 方法名称
2. 方法返回值类型
3. 方法参数类型
4. 方法执行的当前时间
5. 方法执行所需时间

spring cloud
===

version
---

- spring boot: 1.5.9.RELEASE
- spring-cloud: Edgware.RELEASE
-[Spring IO Platform 依赖包](https://docs.spring.io/platform/docs/Brussels-SR6/reference/htmlsingle/#appendix-dependency-versions)
  - docs/save/Appendix A. Dependency versions.html

app
---

- sv-config, port: 8888
- sv-registry, port: 8761
- sv-gateway, port: 4000
- sv-monitoring, port: 8080, Hystrix Dashboard
- demo-service, port: 5001
- auth-service, port: 5000
- notification-service,

config /etc/hosts

```
127.0.0.1 config
127.0.0.1 sv-registry
127.0.0.1 sv-gateway
127.0.0.1 sv-monitoring
127.0.0.1 auth-service
127.0.0.1 demo-service
```

run:
- sv-config, sv-registry, sv-gateway, sv-monitoring
- demo-service

brower: 
    - `http://sv-registry:8761/`, 
    - `http://sv-registry:8761/eureka/apps`
    - `http://localhost:8080/hystrix`
    - demo-service by zuul: `http://localhost:4000/demo/hello`
    
- [Eureka高可用集群](http://tech.lede.com/2017/03/29/rd/server/SpringCloud1C/), 采用相同配置发布

    ```
    server.port=1111
    spring.application.name=eureka-server
    eureka.client.serviceUrl.defaultZone=http://10.120.163.01:1111/eureka/,http://10.120.163.02:1111/eureka/
    ```

- sv-registry 运行成功后，其他 app 的 application.yml 设置了 `eureka.client.serviceUrl.defaultZone=http://sv-registry:8761/eureka/`, 就能自动注册到该服务
- demo-service: Feign客户端
    - TestController, `/testEureka`, 没成功
    

spring cloud
---

### [Spring Cloud技术分析](http://tech.lede.com/2017/03/15/rd/server/SpringCloud0/)

- Eureka, (尤里卡),服务注册和发现; 整合了Consul和Zookeeper作为备选
- Hystrix,调用断路器
- Ribbon, 调用端负载均衡
- 智能服务路由,Zuul
    - 路由方式: url, serviceId
    - [spring cloud zuul 学习](http://tech.lede.com/2017/05/16/rd/server/SpringCloudZuul/)
- Feign, Rest客户端
- sleuth, 提供了对spring cloud系列的链路追踪
- [Application Performance Management](https://www.zhihu.com/question/27994350): zipkin,cat(国内)
- 用于监控数据收集和展示的Spectator、Servo、Atlas，
- 用于配置读取的Archaius和提供Controller层Reactive封装的RxJava

### 参考项目

- [pig](https://gitee.com/log4j/pig)
- [PiggyMetrics](https://github.com/cloudframeworks-springcloud/PiggyMetrics)
- udp
  - [udp blog](https://my.oschina.net/wangkang80/blog), 有空看看
- [AG-Admin](https://gitee.com/geek_qi/ace-security)

    AG-Admin是国内首个基于Spring Cloud微服务化开发平台，具有统一授权、认证后台管理系统，其中包含具备用户管理、资源权限管理、网关API管理等多个模块，支持多业务系统并行开发，可以作为后端服务的开发脚手架。代码简洁，架构清晰，适合学习和直接项目中使用。核心技术采用Eureka、Fegin、Ribbon、Zuul、Hystrix、JWT Token、Mybatis等主要框架和中间件，前端采用vue-element-admin组件。
    
- [zhangxd1989/spring-boot-cloud](https://github.com/zhangxd1989/spring-boot-cloud)
    - 基于 Spring Boot、Spring Cloud、Spring Oauth2 和 Spring Cloud Netflix 等框架构建的微服务项目。
    - [介绍](https://my.oschina.net/yanpenglei/blog/1591159)
    
2018.01.30
---

### @Configuration

sshapp-jpa-common 引入到其他项目，如果项目的包路径与 `org.ssh.boot.conf` 不一致，引入的 jpa-common 配置就无效

example:
- sshapp-jpa-common package: org.ssh.boot
- one app package: org.ssh.cloud.auth

按这种方式启动的话，就找不到 sshapp-jap-common

必须按以下规范:
- sshapp-jpa-common package: org.ssh.boot
- one app package: org.ssh.boot.Application( main application class), 只要保证了启动程序的 package 为 org.ssh.boot

统一以后项目启动类为 `org.ssh.boot.ExampleApplication.class`

2018.01.27
---

### 依赖 sshapp-springboot-parent

```
  <parent>
    <groupId>org.sshapp</groupId>
    <artifactId>sshapp-springboot-parent</artifactId>
    <version>1.5.0</version>
  </parent>
```

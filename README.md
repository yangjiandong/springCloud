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
- sv-monitoring
- auth-service, port: 5000

config /etc/hosts

```
127.0.0.1 config
127.0.0.1 sv-registry
127.0.0.1 sv-gateway
127.0.0.1 sv-monitoring
127.0.0.1 auth-service
```

spring cloud
---

### [Spring Cloud技术分析](http://tech.lede.com/2017/03/15/rd/server/SpringCloud0/)

- Eureka, (尤里卡),服务注册和发现; 整合了Consul和Zookeeper作为备选
- Hystrix,调用断路器
- Ribbon, 调用端负载均衡
- 智能服务路由,Zuul
- Feign, Rest客户端
- 用于监控数据收集和展示的Spectator、Servo、Atlas，
- 用于配置读取的Archaius和提供Controller层Reactive封装的RxJava


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

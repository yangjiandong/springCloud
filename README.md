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

todo
===

## 分布式事务

- [分布式事务解决方案之消息最终一致性方案](https://segmentfault.com/l/1500000012729662)
  - 参考项目[coolmq](https://github.com/vvsuperman/coolmq)
- [从银行转账失败到分布式事务：总结与思考](https://www.cnblogs.com/xybaby/p/7465816.html)

## web ui

- [Fauxton is the new Web UI for CouchDB](https://github.com/apache/couchdb-fauxton)
- react
  - [CoreUI-React](https://github.com/mrholek/CoreUI-React)

## zuul 路由权限

- [zuul 文档](https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html)
- [aouth 服务](https://my.oschina.net/u/3707083/blog/1550787), [2](https://my.oschina.net/u/3707083/blog/1550788)
  - demo/demo-oauth-service
    - 简单版的OAuth服务器, `test/LoginTest`
    - 获得token, `curl client:secret@localhost:8080/oauth/token -d grant_type=password -d username=user -d password=pwd`
  - demo/demo-oauth2-service
    - 自定义验证信息 CustomAuthenticationFailedException
    - 自定义验证 CustomTokenGranter, GRANT_TYPE 为`custom`, `test/LoginCustomTokenTest`
  - 端点安全, `/oauth/authorize`，`/oauth/token`，`/oauth/check_token`，`/oauth/confirm_access`和`/oauth/error`，使用Sprint Boot实现的OAuth服务器默认只保护了`/oauth/token`，由于该服务器有可能会被外部访问，所以需要保护其他三个端点不被随意访问
    
    OauthConfig，只有角色为ROLE_CLIENT才能访问/oauth/check_token
    
    - SecurityConfig, 保护 `/oauth/**`, 但 `oauth/token` 不影响
  - demo/demo-resource-service, 指定 oauth 服务到 `demo-oauth2-service`
    
    测试步骤
    
    - 启动 demo-oauth2-service
    - todo ?
    
- [oauth2-jwt-using-spring](http://www.tinmegali.com/en/oauth2-jwt-using-spring/)

## spring cloud config server auto reload

config 改动后，应用还不能自动刷新

/xxx/refresh 刷新配置(xxx表示服务根路径)，无需重启服务

## startup 数据初始化

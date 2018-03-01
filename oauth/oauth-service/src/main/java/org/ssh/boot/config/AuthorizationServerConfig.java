package org.ssh.boot.config;

import org.ssh.boot.oauthconfig.conf.AuthServerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @author: 林塬
 * @date: 2018/1/10
 * @description: OAuth2 授权服务器配置
 */
@Configuration
public class AuthorizationServerConfig extends AuthServerConfig {

    /**
     * 调用父类构造函数，设置令牌失效日期等信息
     */
    public AuthorizationServerConfig() {
        super((int)TimeUnit.DAYS.toSeconds(1),
            0,
            false,
            false);
    }

    /**
     * 配置客户端详情
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.inMemory()                          // 使用内存存储客户端信息
                .withClient("resource1")       // client_id
                .secret("secret")                   // client_secret
                .authorizedGrantTypes("authorization_code","password")     // 该client允许的授权类型
                .accessTokenValiditySeconds(3600)               // Token 的有效期
                .scopes("read")                    // 允许的授权范围
                .autoApprove(true);                  //登录后绕过批准询问(/oauth/confirm_access)
    }
}

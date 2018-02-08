package org.ssh.boot.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.ssh.boot.CustomTokenGranter;

@Configuration
@ImportResource("classpath:/client.xml")
public class OauthConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenServices(tokenServices(endpoints))
            .authenticationManager(authenticationManager);
        endpoints.tokenGranter(tokenGranter(endpoints));// 加了自定义验证
    }

    // 只有角色为ROLE_CLIENT才能访问/oauth/check_token
    // @Override
    // public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    //     security.checkTokenAccess("hasAuthority('ROLE_CLIENT')");
    // }

    private DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(tokenStore());
        services.setSupportRefreshToken(true);
        services.setReuseRefreshToken(false);
        services.setClientDetailsService(endpoints.getClientDetailsService());
        return services;
    }

    private TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<TokenGranter>(
            Arrays.asList(endpoints.getTokenGranter()));

        granters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager,
            endpoints.getTokenServices(),
            endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        granters.add(new RefreshTokenGranter(endpoints.getTokenServices(),
            endpoints.getClientDetailsService(),
            endpoints.getOAuth2RequestFactory()));

        granters.add(new CustomTokenGranter(endpoints.getTokenServices(),
            endpoints.getClientDetailsService(),
            endpoints.getOAuth2RequestFactory()));

        return new CompositeTokenGranter(granters);
    }
}

package org.ssh.boot.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.ssh.boot.security.EkAccessDecisionManager;
import org.ssh.boot.security.EkInvocationSecurityMetadataSource;
import org.ssh.boot.security.EkJwtAuthenticationEntryPoint;
import org.ssh.boot.security.EkLogoutHandler;
import org.ssh.boot.security.EkLogoutSuccessHandler;
import org.ssh.boot.security.EkResourceMetaServiceImpl;
import org.ssh.boot.security.EkUserDetailsService;
import org.ssh.boot.security.LimitLoginAuthenticationByQueryProvider;
import org.ssh.boot.security.ResourceMetaService;
import org.ssh.boot.security.jwt.JWTAuthenticationFilter;
import org.ssh.boot.security.jwt.JWTLoginFilter;
import org.ssh.boot.security.jwt.TokenAuthenticationService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${login_jwt_route_path:auth}")
    String loginRoutePath;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        LimitLoginAuthenticationByQueryProvider authenticationProvider = new LimitLoginAuthenticationByQueryProvider();
        // LimitLoginAuthenticationProvider authenticationProvider = new LimitLoginAuthenticationProvider();
        authenticationProvider.setHideUserNotFoundExceptions(false);// 报用户没找到的异常
        authenticationProvider.setUserDetailsService(ekUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // 设置密码加密方式
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // 注意，只能有一个 AuthenticationManager
        // 注意可以增加多个登录处理
        auth.authenticationProvider(authenticationProvider());
    }

    // Bcrypt被设计为非常慢以阻碍离线破解
    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义UserDetailsService，从数据库中读取用户信息
     */
    @Bean
    public EkUserDetailsService ekUserDetailsService() {
        return new EkUserDetailsService();
    }

    @Bean
    public ResourceMetaService resourceMetaService() {
        return new EkResourceMetaServiceImpl();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource mySecurityMetadataSource() {
        EkInvocationSecurityMetadataSource securityMetadataSource = new EkInvocationSecurityMetadataSource();
        return securityMetadataSource;
    }

    @Bean("ekTokenAuthenticationService")
    public TokenAuthenticationService myTokenAuthenticationService() {
        return new TokenAuthenticationService();
    }

    @Bean
    public EkJwtAuthenticationEntryPoint unauthorizedHandler() {
        return new EkJwtAuthenticationEntryPoint();
    }

    @Bean
    public AccessDecisionManager myAccessDecisionManager() {
        return new EkAccessDecisionManager();
    }

    void configBywithObjectPostProcessor(HttpSecurity http) throws Exception {
        // withObjectPostProcessor 方案，必须所有url都进行维护
        http.logout().logoutUrl("/logout")
            .addLogoutHandler(new EkLogoutHandler())
            .logoutSuccessHandler((new EkLogoutSuccessHandler(HttpStatus.OK)))
            .and()
            .csrf().disable()
            .authorizeRequests().anyRequest().authenticated()
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                public <O extends FilterSecurityInterceptor> O postProcess(
                    O fsi) {
                    fsi.setSecurityMetadataSource(mySecurityMetadataSource());
                    fsi.setAccessDecisionManager(myAccessDecisionManager());
                    return fsi;
                }
            });
    }

    // 对 configBywithObjectPostProcessor 方式不起作用
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "/static/**",
            "/favicon.ico",
            "/demo/**",
            "/common/**",
            "/druidd/**",
            "/hz/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String loginPath = "/" + loginRoutePath;

        configBywithObjectPostProcessor(http);

        // 禁用缓存
        http.headers().cacheControl();
        http
            // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
            .addFilterBefore(new JWTLoginFilter(loginPath, authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
            // 添加一个过滤器验证其他请求的Token是否合法
            .addFilterBefore(new JWTAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }
}

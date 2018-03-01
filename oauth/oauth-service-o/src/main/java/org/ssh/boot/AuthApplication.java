package org.ssh.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.ssh.boot.id.SnowFlake;
import org.ssh.boot.utils.SpringContextHolder;
import org.ssh.boot.utils.cache.EhCache;
import org.ssh.boot.utils.cache.ICache;
import org.ssh.boot.utils.cache.RedisCache;

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@ImportResource({"classpath:/applicationContext-sshapp.xml"})
public class AuthApplication {

    @Value("${cache_type:ehcache}")
    String cacheType;

    // ID 生产方案
    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(0, 0);
    }

    // cache
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("/ehcache.xml"));
        return cacheManagerFactoryBean;
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
        return new EhCacheCacheManager(bean.getObject());
    }

    @Bean
    public ICache getCacheBean() {
        if (cacheType.equals("redis")) {
            return new RedisCache();
        } else {
            return new EhCache();
        }
    }

    @Bean
    public SpringContextHolder getSpringContextHolder() {
        return new SpringContextHolder();
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}

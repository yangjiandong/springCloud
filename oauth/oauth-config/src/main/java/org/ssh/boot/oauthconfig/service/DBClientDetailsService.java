package org.ssh.boot.oauthconfig.service;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

/**
 * @author: one
 * @date: 2018/1/22
 * @description: 通过数据库加载客户端详情
 */
public class DBClientDetailsService {

    @Autowired
    private DataSource dataSource;

    @Bean
    public ClientDetailsService clientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }

}

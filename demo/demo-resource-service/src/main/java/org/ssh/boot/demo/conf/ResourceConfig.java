package org.ssh.boot.demo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").
            authorizeRequests().anyRequest().access("hasAuthority('ROLE_USER')")
            // .and()
            // .sessionManagement()
            // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }

}

package org.ssh.boot;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 测试错误用户
        if ("failed_user".equals(username)) {
            throw new CustomAuthenticationFailedException(7);
        }
        return new User("user", "pwd", AuthorityUtils.createAuthorityList("ROLE_USER"));
    }

}

package org.ssh.boot.security;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.ssh.boot.auth.entity.Role;
import org.ssh.boot.auth.entity.User;
import org.ssh.boot.auth.service.UserService;


@Slf4j
public class EkUserDetailsService implements UserDetailsService {

    @Autowired  //数据库服务类
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userService.findUserByName(userName);

        if (user == null) {
            throw new UsernameNotFoundException("用户 " + userName + " 不存在");
        }

        log.info("get user: {}", user);

        //
        List<Role> userRoles = userService.findRolesByUserName(userName);
        log.info("get roles: {}", userRoles);
        return new EkUserDetails(user, userRoles);
    }

}

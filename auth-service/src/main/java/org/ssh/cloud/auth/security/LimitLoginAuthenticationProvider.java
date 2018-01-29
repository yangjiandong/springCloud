package org.ssh.cloud.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.ssh.boot.utils.DateUtils;
import org.ssh.cloud.auth.entity.User;
import org.ssh.cloud.auth.service.UserService;

// http://www.mkyong.com/spring-security/spring-security-limit-login-attempts-example/
// TODO
// 暂时不用，全部采用缓存策略还有问题
@Slf4j
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        try {

            userService.limitLoginCache(authentication.getName());
            //userAuthService.limitLoginThrowLoginLimitNumException(authentication.getName());

            Authentication auth = super.authenticate(authentication);

            //if reach here, means login success, else exception will be thrown
            //reset the user_attempts
            userService.resetFailAttemptsCache(authentication.getName());

            return auth;

        } catch (BadCredentialsException e) {
            //log.error("", e);

            //invalid login, update to user_attempts
            // 登录失败
            userService.updateFailAttemptsCache(authentication.getName());
            throw e;

        } catch (LockedException e) {
            //log.error("", e);

            //this user is locked!
            String error;
            User user = userService.findByUsername(authentication.getName());
            if (user != null) {
                String lockedAt = DateUtils.getLocalDateTimeString(DateUtils.getDateTimeFromTimestamp(user.getLockedAt()));
                error = "User account is locked! <br><br>Username : " + authentication.getName()
                    + "<br>locked at : " + lockedAt;
            } else {
                error = e.getMessage();
            }

            throw new LockedException(error);
        }

    }

}

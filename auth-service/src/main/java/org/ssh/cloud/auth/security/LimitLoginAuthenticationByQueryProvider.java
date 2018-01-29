package org.ssh.cloud.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.ssh.boot.security.SecurityConstant;
import org.ssh.boot.utils.DateUtils;
import org.ssh.cloud.auth.entity.User;
import org.ssh.cloud.auth.service.UserAuthService;
import org.ssh.cloud.auth.service.UserService;

// http://www.mkyong.com/spring-security/spring-security-limit-login-attempts-example/
// 通过查询表
@Slf4j
public class LimitLoginAuthenticationByQueryProvider extends DaoAuthenticationProvider {

  @Autowired
  UserAuthService userAuthService;
  @Autowired
  UserService userService;
  @Value("${login_max_attempts:3}")
  int MAX_ATTEMPTS;
  @Value("${login_locked_wait_times:180}")
  int login_locked_wait_times; // 60*3

  @Override
  public Authentication authenticate(Authentication authentication)
    throws AuthenticationException {

    try {

      userAuthService.limitLogin(authentication.getName());
      // userAuthService.limitLoginThrowLoginLimitNumException(authentication.getName());

      Authentication auth = super.authenticate(authentication);

      //if reach here, means login success, else exception will be thrown
      //reset the user_attempts
      userAuthService.resetFailAttempts(authentication.getName());

      return auth;

    } catch (BadCredentialsException e) {
      //log.error("", e);

      //invalid login, update to user_attempts
      // 登录失败
      userAuthService.updateFailAttempts(authentication.getName());
      throw e;

    } catch (LockedException e) {
      //log.error("", e);

      //this user is locked!
      String error;
      User user = userService.findByUsername(authentication.getName());
      if (user != null) {
        //String lockedAt = DateUtils.getLocalDateTimeString(DateUtils.getDateTimeFromTimestamp(user.getLockedAt()));
        //error = "User account is locked! <br><br>Username : " + authentication.getName()
        //    + "<br>locked at : " + lockedAt;
        long times = (DateUtils.getSystemTimeMilliseconds() - user.getLockedAt()) / 1000L;
        log.info("times: {}, locked user: {}", times, user);

        error = String.format(SecurityConstant.LIMIT_MAX_ATTEMPTS, MAX_ATTEMPTS,
          (login_locked_wait_times - times) + "秒");

      } else {
        error = e.getMessage();
      }

      // TODO
      // 采用 ehcache, 重启系统，ehcache 中信息丢失，只会报 "当前用户已被锁住，暂时不能登录"
      throw new LockedException(error);
    }

  }

}

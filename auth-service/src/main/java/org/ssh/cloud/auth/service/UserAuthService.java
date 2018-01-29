package org.ssh.cloud.auth.service;

import static org.ssh.cloud.auth.AppConstants.CACHE_LOGIN_LIMIT;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.mapper.JsonMapper;
import org.ssh.boot.security.LoginLimitNumException;
import org.ssh.boot.security.SecurityConstant;
import org.ssh.boot.utils.DateUtils;
import org.ssh.boot.utils.cache.ICache;
import org.ssh.cloud.auth.dao.UserDao;
import org.ssh.cloud.auth.entity.User;

@Slf4j
@Component
@Transactional("transactionManagerSys")
public class UserAuthService {
    @Autowired
    ICache cache;

    @Autowired
    UserDao userDao;
    @Value("${login_max_attempts_times:60}")
    int MAX_ATTEMPTS_TIMES;
    @Value("${login_max_attempts:3}")
    int MAX_ATTEMPTS;
    @Value("${login_locked_wait_times:180}")
    int login_locked_wait_times; // 60*3

    public void resetFailAttempts(String userName) {
        User user = userDao.findByUsername(userName);
        if (user == null) {
            return;
        }
        resetFailAttempts(user);
    }

    public void updateFailAttempts(String userName) {
        User user = userDao.findByUsername(userName);
        if (user == null) {
            return;
        }

        // // TODO
        // // 此次有个异常处理，
        // if (user.getAttempts() >= MAX_ATTEMPTS) {
        //     // lock user
        //     userDao.updateLocked(false, DateUtils.getSystemTimeMilliseconds(), userName);
        //     // throw exception
        //     throw new LockedException("用户暂时被锁");
        // } else
        if (user.getAttempts() == 0) {
            updateFirstAttempts(DateUtils.getSystemTimeMilliseconds(), user);
        } else {
            // update attempts count, +1
            updateAttempts(user);
        }

    }

    // 检查是否超出规定时间
    // 比如规定1分钟只能登录3次，如果过了1分钟，就归零
    public void checkAttempTimes(String userName) {
        User user = userDao.findByUsername(userName);
        if (user == null) {
            return;
        }

        if (user.getFirstAttempt() > 0) {
            long attemptTimes =
                (DateUtils.getSystemTimeMilliseconds() - user.getFirstAttempt()) / 1000L;
            log.info("attemptTimes: {}", attemptTimes);

            if (attemptTimes > MAX_ATTEMPTS_TIMES && user.getAttempts() < MAX_ATTEMPTS) {
                // 超出规定时候，解锁
                resetFailAttempts(user);
                // TODO 没有更新数据
                //user = userDao.findByUsername(userName);
                //log.info("重新复原get user: {}", user);
            }
        }
    }

    // 限制登录次数
    public void limitLogin(String userName) {
        // 先取缓存，如果有说明用户被锁
        String key = userName;
        String jsonString = cache.getValue(CACHE_LOGIN_LIMIT, key);

        if (!StringUtils.isBlank(jsonString)) {
            User userO = JsonMapper.INSTANCE.fromJson(jsonString, User.class);
            long times = (DateUtils.getSystemTimeMilliseconds() - userO.getLockedAt()) / 1000L;
            log.info("times: {}, locked user: {}", times, userO);
            throw new LoginLimitNumException(
                String.format(SecurityConstant.LIMIT_MAX_ATTEMPTS, MAX_ATTEMPTS,
                    (login_locked_wait_times - times) + "秒"));
        }
        //

        User user = userDao.findByUsername(userName);
        if (user == null) {
            return;
        }
        if (user.getFirstAttempt() > 0) {
            long attemptTimes =
                (DateUtils.getSystemTimeMilliseconds() - user.getFirstAttempt()) / 1000L;
            log.info("attemptTimes: {}", attemptTimes);

            if (attemptTimes > MAX_ATTEMPTS_TIMES && user.getAttempts() < MAX_ATTEMPTS) {
                // 超出规定时候，解锁
                resetFailAttempts(user);
            }
        }

        if (user.getAttempts() >= MAX_ATTEMPTS) {
            if (!user.isAccountNonLocked()) {
                // 如果锁住了
                long times = (DateUtils.getSystemTimeMilliseconds() - user.getLockedAt()) / 1000L;
                log.info("login_locked_wait_times: {}", times);
                // 如果满足等待时间
                if (times > login_locked_wait_times) {
                    // 解锁
                    resetFailAttempts(user);
                }
            } else {
                // lock user
                // 异常触发不保存数据
                updateLocked(DateUtils.getSystemTimeMilliseconds(), user);
            }

        } else {
            // 异常触发重新处理，不然以上操作事务就回滚了
        }

    }

    public void limitLoginThrowLoginLimitNumException(String userName) {
        User user = userDao.findByUsername(userName);
        if (user == null) {
            return;
        }

        if (user.getAttempts() >= MAX_ATTEMPTS) {

            if (!user.isAccountNonLocked()) {
                long times = (DateUtils.getSystemTimeMilliseconds() - user.getLockedAt()) / 1000L;

                if (times <= login_locked_wait_times) {

                    // throw exception, String.format("value is %d", 32)
                    throw new LoginLimitNumException(
                        String.format(SecurityConstant.LIMIT_MAX_ATTEMPTS, MAX_ATTEMPTS,
                            (login_locked_wait_times - times) + "秒"));
                }
            }
        }

    }

    void resetFailAttempts(User user) {
        // UPDATE User SET attempts = 0, firstAttempt = 0, lockedAt = 0,  accountNonLocked = true WHERE username = :userName
        user.setAttempts(0);
        user.setFirstAttempt(0L);
        user.setLockedAt(0L);
        user.setAccountNonLocked(true);
        userDao.save(user);

        cache.removeValue(CACHE_LOGIN_LIMIT, user.getUsername());
    }

    void updateFirstAttempts(long times, User user) {
        user.setAttempts(1);
        user.setFirstAttempt(times);
        userDao.save(user);
    }
    //UPDATE User SET attempts = 1, firstAttempt = :firstAttempt WHERE username = :userName

    void updateAttempts(User user) {
        // UPDATE User SET attempts = attempts + 1 WHERE username = :userName
        user.setAttempts(user.getAttempts() + 1);
        userDao.save(user);
    }

    void updateLocked(long lockedAt, User user) {
        // UPDATE User SET accountNonLocked = :accountNonLocked, lockedAt = :lockedAt WHERE username = :userName
        user.setAccountNonLocked(false);
        user.setLockedAt(lockedAt);
        userDao.save(user);

        cache.setValue(CACHE_LOGIN_LIMIT, user.getUsername(), JsonMapper.INSTANCE.toJson(user), login_locked_wait_times);
    }
}

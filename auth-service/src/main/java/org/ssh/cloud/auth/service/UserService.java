package org.ssh.cloud.auth.service;

import static org.ssh.cloud.auth.AppConstants.CACHE_LOGIN_LIMIT;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.mapper.JsonMapper;
import org.ssh.boot.security.LoginLimitNumException;
import org.ssh.boot.security.SecurityConstant;
import org.ssh.boot.utils.DateUtils;
import org.ssh.boot.utils.cache.ICache;
import org.ssh.cloud.auth.dao.ResourceDao;
import org.ssh.cloud.auth.dao.RoleDao;
import org.ssh.cloud.auth.dao.RoleResourceDao;
import org.ssh.cloud.auth.dao.UserDao;
import org.ssh.cloud.auth.dao.UserRoleDao;
import org.ssh.cloud.auth.entity.Resource;
import org.ssh.cloud.auth.entity.Role;
import org.ssh.cloud.auth.entity.RoleResource;
import org.ssh.cloud.auth.entity.User;
import org.ssh.cloud.auth.entity.UserRole;
import org.ssh.cloud.auth.mapper.UserMapper;

@Slf4j
@Component
@Transactional("transactionManagerSys")
public class UserService {

  @Autowired
  ICache cache;
  @Autowired
  UserDao userDao;
  @Autowired
  RoleDao roleDao;
  @Autowired
  UserRoleDao userRoleDao;
  @Autowired
  ResourceDao resourceDao;
  @Autowired
  RoleResourceDao roleResourceDao;
  @Autowired
  @Qualifier("passwordEncoder")
  PasswordEncoder passwordEncoder;
  @Autowired
  UserMapper userMapper;
  @Value("${login_max_attempts_times:60}")
  int MAX_ATTEMPTS_TIMES;
  @Value("${login_max_attempts:3}")
  int MAX_ATTEMPTS;
  @Value("${login_locked_wait_times:180}")
  int login_locked_wait_times; // 60*3
  @Value("${login_default_password:123}")
  String default_password;
  @Autowired
  ResourceLoader resourceLoader;

  @Profile("test")
  public void removeDatas() {
    userDao.deleteAllInBatch();
    roleDao.deleteAllInBatch();
    userRoleDao.deleteAllInBatch();
    resourceDao.deleteAllInBatch();
    roleResourceDao.deleteAllInBatch();
    initData();
  }

  @Transactional(readOnly = true)
  public User findUserByName(String name) {
    return userDao.findByUsername(name);
  }

  public void initData() {
    if (userDao.count() > 0) {
      return;
    }

    Role roleAdmin = new Role();
    roleAdmin.setRoleName(SecurityConstant.ADMIN_ROLE);
    roleAdmin.setRoleDescription("系统管理员");
    roleDao.save(roleAdmin);
    Long roleAdminId = roleAdmin.getId();
    //
    Role roleUser = new Role();
    roleUser.setRoleName(SecurityConstant.USER_ROLE);
    roleUser.setRoleDescription("基本用户");
    roleDao.save(roleUser);
    Long roleUserId = roleUser.getId();

    //admin user
    User user = new User();
    user.setUsername("admin");
    user.setPassword(passwordEncoder.encode(default_password));
    user.setEnabled(true);
    user.setAccountNonLocked(true);
    user.setAccountNonExpired(true);
    user.setCredentialsNonExpired(true);
    user.setName("系统管理员");
    user.setIntros("个性风云");
    user.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    userDao.save(user);

    UserRole userRole = new UserRole();
    userRole.setUid(user.getId());
    userRole.setRid(roleAdmin.getId());
    userRoleDao.save(userRole);

    // demo user
    user = new User();
    user.setUsername("demo");
    user.setPassword(passwordEncoder.encode(default_password));
    user.setEnabled(true);
    user.setAccountNonLocked(true);
    user.setAccountNonExpired(true);
    user.setCredentialsNonExpired(true);
    user.setName("测试用户");
    user.setIntros("测试用户-个性风云");
    user.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    userDao.save(user);

    userRole = new UserRole();
    userRole.setUid(user.getId());
    userRole.setRid(roleUser.getId());
    userRoleDao.save(userRole);

    //resources
    try {
      initResource((resource, isAdmin) -> {
        RoleResource roleResource = new RoleResource();
        if (isAdmin.equals("1")) {
          roleResource.setRid(roleAdminId);
          roleResource.setRaid(resource.getId());
          roleResourceDao.save(roleResource);
        } else {
          roleResource.setRid(roleUserId);
          roleResource.setRaid(resource.getId());
          roleResourceDao.save(roleResource);

          roleResource = new RoleResource();
          roleResource.setRid(roleAdminId);
          roleResource.setRaid(resource.getId());
          roleResourceDao.save(roleResource);
        }
      });
    } catch (IOException e) {
      log.error("初始化菜单出错: ", e);
    }

  }

  public void initResource(BiConsumer<Resource, String> biConsumer) throws IOException {
    org.springframework.core.io.Resource res = resourceLoader
      .getResource("classpath:data/resource.txt");
    String thisLine;

    DataInputStream myInput = new DataInputStream(res.getInputStream());
    BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));
    int line = 1;
    Resource resource;

    while ((thisLine = br.readLine()) != null) {
      //第一行是标题
      if (line == 1) {
        line++;
        continue;
      }
      String star[] = StringUtils.splitPreserveAllTokens(thisLine, ",");

      if (resourceDao.findByName(star[0]) != null) {
        continue;
      }
      resource = new Resource();
      resource.setName(star[0]);
      resource.setPath(star[1]);
      resourceDao.save(resource);

      // use Refactor -> Extract -> Function Parameter , Convert to functional expression
      // RoleResource roleResource = new RoleResource();
      // if (star[2].equals("1")) {
      //     roleResource.setRid(roleAdminId);
      //     roleResource.setRaid(resource.getId());
      //     roleResourceDao.save(roleResource);
      // } else {
      //     roleResource.setRid(roleUserId);
      //     roleResource.setRaid(resource.getId());
      //     roleResourceDao.save(roleResource);
      //
      //     roleResource = new RoleResource();
      //     roleResource.setRid(roleAdminId);
      //     roleResource.setRaid(resource.getId());
      //     roleResourceDao.save(roleResource);
      // }
      String isAdmin = star[2];
      biConsumer.accept(resource, isAdmin);

    }
  }

  public void setAccountExpired(String userName) {
    User user = userDao.findByUsername(userName);
    user.setAccountNonExpired(false);
    user.setExpiredAt(DateUtils.getSystemTimeMilliseconds());
    userDao.save(user);
  }

  public void setAccountNoExpired(String userName) {
    User user = userDao.findByUsername(userName);
    user.setAccountNonExpired(true);
    user.setExpiredAt(0L);
    userDao.save(user);
  }

  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userDao.findAll();
  }

  @Transactional(readOnly = true)
  public User get(Long id) {
    return userDao.getOne(id);
  }

  // 没有 lazy
  @Transactional(readOnly = true)
  public User find(Long id) {
    return userDao.findOne(id);
  }

  @Transactional(readOnly = true)
  public List<User> myQueryUsers() {
    List<User> users = userMapper.getAll();
    for (User one : users) {
      log.info("user: {}", one);
    }
    return users;
  }

  @Transactional(readOnly = true)
  public List<String> findRoleNameByUserName(String userName) {
    return userRoleDao.findRoleNameByUserName(userName);
  }

  @Transactional(readOnly = true)
  public List<Role> findRolesByUserName(String userName) {
    return userRoleDao.findRolesByUserName(userName);
  }

  @Transactional(readOnly = true)
  public User findByUsername(String userName) {
    return userDao.findByUsername(userName);
  }

  public void updateFailAttemptsCache(String userName) {

    // String key = userName;
    String jsonString = cache.getValue(CACHE_LOGIN_LIMIT, userName);
    User user;
    if (!StringUtils.isBlank(jsonString)) {
      user = JsonMapper.INSTANCE.fromJson(jsonString, User.class);
    } else {
      user = userDao.findByUsername(userName);
    }

    if (user.getAttempts() == 0) {
      user.setAttempts(1);
      user.setFirstAttempt(DateUtils.getSystemTimeMilliseconds());
    } else {
      user.setAttempts(user.getAttempts() + 1);
    }
    jsonString = JsonMapper.INSTANCE.toJson(user);
    cache.setValue(CACHE_LOGIN_LIMIT, userName, jsonString, login_locked_wait_times);
  }

  public void resetFailAttemptsCache(String userName) {
    cache.removeValue(CACHE_LOGIN_LIMIT, userName);
  }

  // 限制登录次数
  public void limitLoginCache(String userName) {
    // String key = userName;
    String jsonString = cache.getValue(CACHE_LOGIN_LIMIT, userName);
    User user;
    if (!StringUtils.isBlank(jsonString)) {
      user = JsonMapper.INSTANCE.fromJson(jsonString, User.class);
    } else {
      return;
    }
    log.info("limit user: {}", user);
    if (user.getFirstAttempt() > 0) {
      long attemptTimes =
        (DateUtils.getSystemTimeMilliseconds() - user.getFirstAttempt()) / 1000L;
      log.info("attemptTimes: {}", attemptTimes);

      if (attemptTimes > MAX_ATTEMPTS_TIMES && user.getAttempts() < MAX_ATTEMPTS) {
        // 超出规定时候，解锁
        resetFailAttemptsCache(userName);
        return;
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
          resetFailAttemptsCache(userName);
          // return;
        } else {
          // throw exception, String.format("value is %d", 32)
          throw new LoginLimitNumException(
            String.format(SecurityConstant.LIMIT_MAX_ATTEMPTS, MAX_ATTEMPTS,
              (login_locked_wait_times - times) + "秒"));
        }
      } else {
        // lock user
        // 异常触发不保存数据
        updateLockedCache(DateUtils.getSystemTimeMilliseconds(), user, userName);
        // throw exception, String.format("value is %d", 32)
        throw new LoginLimitNumException(
          String.format(SecurityConstant.LIMIT_MAX_ATTEMPTS, MAX_ATTEMPTS,
            (login_locked_wait_times) + "秒"));
      }

    }
  }

  void updateLockedCache(long lockedAt, User user, String key) {
    // UPDATE User SET accountNonLocked = :accountNonLocked, lockedAt = :lockedAt WHERE username = :userName
    user.setAccountNonLocked(false);
    user.setLockedAt(lockedAt);
    String jsonString = JsonMapper.INSTANCE.toJson(user);
    cache.setValue(CACHE_LOGIN_LIMIT, key, jsonString, login_locked_wait_times);

  }


}


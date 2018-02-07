package org.ssh.boot;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.ssh.boot.auth.service.UserService;

//初始化
@Component
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UserService userService;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        log.info("start onApplicationEvent ...");

        // 执行多次的解决
        if (event.getApplicationContext().getParent() == null) {
            log.info("start...");

            initUser();
            initResource();
        }
    }

    void initUser() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start("生成User");
        userService.initData();
        stopwatch.stop();
        log.info("{}, {}", stopwatch.getLastTaskName(), stopwatch.getTotalTimeSeconds());
    }

    void initResource() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start("生成Resource");
        try {
            userService.initResource((resource, isAdmin) -> {
            });
        } catch (IOException e) {
            log.error("初始化菜单出错: ", e);
        }
        stopwatch.stop();
        log.info("{}, {}", stopwatch.getLastTaskName(), stopwatch.getTotalTimeSeconds());
    }

}

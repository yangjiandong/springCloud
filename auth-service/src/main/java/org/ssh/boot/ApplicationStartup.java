package org.ssh.boot;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.ssh.boot.auth.service.UserService;

//初始化
// http://blog.netgloo.com/2014/11/13/run-code-at-spring-boot-startup/
@Component
@Slf4j
public class ApplicationStartup
    // before 1.3
    //implements ApplicationListener<ContextRefreshedEvent> {
    implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    UserService userService;

    // @Override
    // public void onApplicationEvent(final ContextRefreshedEvent event) {
    //     log.info("start onApplicationEvent ...");
    //
    //     // 执行多次的解决
    //     if (event.getApplicationContext().getParent() == null) {
    //         log.info("start...");
    //
    //         initUser();
    //         initResource();
    //     }
    // }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.info("start onApplicationEvent ...");

        initUser();
        initResource();
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

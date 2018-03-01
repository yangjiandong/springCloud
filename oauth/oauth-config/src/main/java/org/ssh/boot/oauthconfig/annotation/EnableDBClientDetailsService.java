package org.ssh.boot.oauthconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.ssh.boot.oauthconfig.service.DBClientDetailsService;

/**
 * @author: one
 * @date: 2018/1/22
 * @description: 在启动类上添加该注解来----开启从数据库加载客户端详情
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DBClientDetailsService.class)
public @interface EnableDBClientDetailsService {
}

package org.ssh.boot.oauthconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.ssh.boot.oauthconfig.service.RemoteTokenService;

/**
 * @author: one
 * @date: 2018/1/22
 * @description: 在启动类上添加该注解来----开启通过授权服务器验证访问令牌（适用于 JDBC、内存存储令牌）
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RemoteTokenService.class)
public @interface EnableRemoteTokenService {
}

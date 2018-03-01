package org.ssh.boot.oauthconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.ssh.boot.oauthconfig.store.ResJWTTokenStore;

/**
 * @author: one
 * @date: 2018/1/22
 * @description: 在启动类上添加该注解来----开启 JWT 令牌存储（资源服务器-非对称加密）
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ResJWTTokenStore.class)
public @interface EnableResJWTTokenStore {
}

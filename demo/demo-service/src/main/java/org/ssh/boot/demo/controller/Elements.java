package org.ssh.boot.demo.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "elements", fallback = ElementsFallback.class) //指定feign调用的服务和Hystrix Fallback（name即eureka的application name）
public interface Elements
{
    @RequestMapping(value = "/index")
    String index();
}

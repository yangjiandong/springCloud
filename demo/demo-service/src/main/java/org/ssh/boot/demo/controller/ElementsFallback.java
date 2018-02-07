package org.ssh.boot.demo.controller;

import org.springframework.stereotype.Component;

//Hystrix Fallback
@Component
public class ElementsFallback implements Elements {

    @Override
    public String index() {
        return "**************";
    }
}

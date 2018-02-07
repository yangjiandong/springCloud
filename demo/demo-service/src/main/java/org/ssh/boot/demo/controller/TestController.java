package org.ssh.boot.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    Elements elements;

    @RequestMapping(value = "/testEureka", method = RequestMethod.GET)
    public String testeureka() {
        return elements.index();
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        return "你好，世界";
    }
}

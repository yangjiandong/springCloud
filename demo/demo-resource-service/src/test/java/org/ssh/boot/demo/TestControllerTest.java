package org.ssh.boot.demo;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testWithMap() {
        MultiValueMap requestBody = new LinkedMultiValueMap();
        // requestBody.add("username", "user");
        // requestBody.add("password", "pwd");
        // requestBody.set("grant_type", "password");
        //
        // // 取得 token
        // Map token = this.restTemplate.withBasicAuth("client1", "secret1")
        //     .postForObject("http://localhost:8080/oauth/token", requestBody, Map.class);
        //
        // log.info("result: {}", token.toString());
        // assertThat(token.get("access_token").equals("")).isFalse();

        String access_token = "";//(String) token.get("access_token");
        requestBody = new LinkedMultiValueMap();
        requestBody.add("access_token", access_token);
        String result = this.restTemplate.postForObject("/hello", requestBody, String.class);

        log.info("get hello resource, result: {}", result);

    }
}

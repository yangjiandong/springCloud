package org.ssh.boot;

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
public class LoginFailedTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testLoginWithMap() {
        MultiValueMap requestBody = new LinkedMultiValueMap();
        requestBody.add("username", "failed_user");
        requestBody.add("password", "pwd");
        requestBody.set("grant_type", "password");

        @SuppressWarnings("unchecked")
        Map<String, Object> token = this.restTemplate.withBasicAuth("client1", "secret1")
            .postForObject("/oauth/token", requestBody, Map.class);

        log.info("result: {}", token.toString());
        assertThat(token.get("error_description").equals("Authentication failed")).isTrue();
        assertThat(token.get("attempt").equals("7")).isTrue();
    }
}

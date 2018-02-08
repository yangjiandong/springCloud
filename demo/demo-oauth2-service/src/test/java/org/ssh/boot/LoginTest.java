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
public class LoginTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testLoginWithMap() {
        MultiValueMap requestBody = new LinkedMultiValueMap();
        requestBody.add("username", "user");
        requestBody.add("password", "pwd");
        requestBody.set("grant_type", "password");

        //@SuppressWarnings("unchecked")
        //Map<String, Object> token = this.testRestTemplate.withBasicAuth(CLIENT_NAME, CLIENT_PASSWORD)
        //    .postForObject(SyntheticsConstants.OAUTH_ENDPOINT, request, Map.class);

        // TResult result
        @SuppressWarnings("unchecked")
        Map<String, Object> token = this.restTemplate.withBasicAuth("client1", "secret1")
            .postForObject("/oauth/token", requestBody, Map.class);

        log.info("result: {}", token.toString());
        assertThat(token.get("access_token").equals("")).isFalse();
        //assertThat(result.isSuccess()).isTrue();

        //result = this.restTemplate.postForObject("/logOut", requestBody, TResult.class);
    }

    // @Test
    // public void testLoginWithResult() {
    //     MultiValueMap requestBody = new LinkedMultiValueMap();
    //     requestBody.add("username", "user");
    //     requestBody.add("password", "pwd");
    //     requestBody.set("grant_type", "password");
    //
    //     //
    //     TResult result = this.restTemplate.withBasicAuth("client1", "secret1")
    //         .postForObject("/oauth/token", requestBody, TResult.class);
    //
    //     log.info("result: {}", result.toString());
    // }
}

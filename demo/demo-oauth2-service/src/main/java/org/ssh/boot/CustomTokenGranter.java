package org.ssh.boot;

import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

// 自定义验证
public class CustomTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "custom";

    public CustomTokenGranter(AuthorizationServerTokenServices tokenServices,
        ClientDetailsService clientDetailsService,
        OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client,
        TokenRequest tokenRequest) {

        Map<String, String> param = tokenRequest.getRequestParameters();
        String username = param.get("username");
        if (!Pattern.matches("[0-9a-zA-Z]*", username)) {
            throw new InvalidRequestException("Invalid username");
        }

        Authentication auth = new AnonymousAuthenticationToken("NA", username,
            AuthorityUtils.createAuthorityList("ROLE_USER"));
        OAuth2Authentication oauth2Auth = new OAuth2Authentication(
            tokenRequest.createOAuth2Request(client), auth);
        return oauth2Auth;

    }

}

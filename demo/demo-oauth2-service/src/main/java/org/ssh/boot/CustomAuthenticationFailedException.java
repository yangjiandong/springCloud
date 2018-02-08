package org.ssh.boot;

import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

@SuppressWarnings("serial")
public class CustomAuthenticationFailedException extends UnauthorizedUserException {

    public CustomAuthenticationFailedException(int attempt) {
        super("Authentication failed");
        addAdditionalInformation("attempt", String.valueOf(attempt));
    }

}

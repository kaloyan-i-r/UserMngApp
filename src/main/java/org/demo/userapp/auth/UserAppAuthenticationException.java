package org.demo.userapp.auth;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by k on 8/1/2015.
 */
public class UserAppAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public UserAppAuthenticationException(String message) {
        super(message);
    }
}

package org.demo.userapp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.userapp.model.entities.User;
import org.demo.userapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider, AuthenticationEntryPoint, LogoutSuccessHandler {

    @Autowired
    private UserService ulService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authenticationUsername = null;
        String authenticationPassword = null;

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            authenticationUsername = String.valueOf(authentication.getPrincipal());
            authenticationPassword = String.valueOf(authentication.getCredentials());
        } else {
            throw new UserAppAuthenticationException("Invalid authentication method.");
        }

        User user = ulService.getUserByEmail(authenticationUsername);

        if (user != null) {

            if (user.getPassword().equals(authenticationPassword) && authenticationPassword != null && !authenticationPassword.isEmpty()) {

                String roleName = user.getRole() != null ? user.getRole() : "";

                if (USER_ROLES.ADMIN.toString().equals(roleName)) {
                    SimpleGrantedAuthority adminUserAuth = new SimpleGrantedAuthority(roleName);
                    Authentication usernamePasswordAuth = new UsernamePasswordAuthenticationToken(
                            authenticationUsername, authenticationPassword, Arrays
                            .asList(new GrantedAuthority[]{adminUserAuth}));
                    return usernamePasswordAuth;
                } else {
                    throw new UserAppAuthenticationException("Insufficient rights.");
                }
            } else {
                throw new UserAppAuthenticationException("Invalid credentials.");
            }
        } else {
            throw new UserAppAuthenticationException("Invalid credentials.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();

    }

    @Component
    public static class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException exception) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            PrintWriter writer = response.getWriter();
            writer.write(exception.getMessage());
            writer.flush();
        }
    }

    @Component
    public static class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(AuthSuccessHandler.class);
        private final ObjectMapper mapper;

        AuthSuccessHandler() {
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            this.mapper = converter.getObjectMapper();
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);

            UserRoles userRoles = null;

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
                String username = (String) token.getPrincipal();
                String role = null;
                for (GrantedAuthority authority : token.getAuthorities()) {
                    role = authority.getAuthority();
                }
                userRoles = new UserRoles(username, role);
            }

            PrintWriter writer = response.getWriter();
            mapper.writeValue(writer, userRoles);
            writer.flush();
        }

        public static class UserRoles {
            private String username;
            private String role;

            public UserRoles(String username, String role) {
                this.username = username;
                this.role = role;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }
        }
    }
}

package org.demo.userapp.config;

import org.demo.userapp.auth.AuthenticationProviderImpl;
import org.demo.userapp.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationProviderImpl authProviderImpl;
    @Autowired
    private AuthenticationProviderImpl.AuthSuccessHandler authSuccessHandler;
    @Autowired
    private AuthenticationProviderImpl.AuthFailureHandler authFailureHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        AuthenticationProviderImpl authenticationProvider = new AuthenticationProviderImpl();

        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().
                antMatchers("/").permitAll().
                antMatchers("/bower_components/**").permitAll().
                antMatchers("/js/**").permitAll().
                antMatchers("/css/**").permitAll().
                antMatchers("/index.html").permitAll().
                antMatchers("/favicon.ico").permitAll().
                antMatchers("/partials/**").permitAll().
                antMatchers("/rest/**").permitAll().
                antMatchers(HttpMethod.POST, "/login/form").permitAll().
                anyRequest().authenticated().
                and().
                authenticationProvider(authenticationProvider()).
                exceptionHandling().
                authenticationEntryPoint(authProviderImpl).
                and().
                formLogin().
                loginProcessingUrl("/login/form").
                usernameParameter("username").
                passwordParameter("password").
                successHandler(authSuccessHandler).
                failureHandler(authFailureHandler).
                loginPage("/login/form").
                and().
                logout().
                logoutSuccessHandler(authProviderImpl).
                logoutSuccessUrl("/login/form").permitAll().
                and().
                sessionManagement().
                maximumSessions(1);
    }

}

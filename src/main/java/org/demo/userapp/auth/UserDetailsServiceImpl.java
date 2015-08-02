package org.demo.userapp.auth;

import org.demo.userapp.model.entities.User;
import org.demo.userapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Autowired
    UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User userLogin = userService.getUserByEmail(userName);
        if (userLogin == null) {
            throw new UsernameNotFoundException("User " + userName + " not found.");
        }
        return new UserDetailsImpl(userLogin);
    }

}

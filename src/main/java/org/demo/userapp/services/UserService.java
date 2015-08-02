package org.demo.userapp.services;

import org.demo.userapp.auth.USER_ROLES;
import org.demo.userapp.error.UserMngAppExceptionMessage;
import org.demo.userapp.model.entities.User;
import org.demo.userapp.model.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by k on 7/30/2015.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }

    public User create(User user) {
        user.setId(null);
        user.setRole(USER_ROLES.USER.toString());
        return userRepository.saveAndFlush(user);
    }

    public void delete(long id) {
        User user = userRepository.findOne(id);
        if (user == null){
            throw new UserMngAppExceptionMessage("No such user!");
        } else if ( user.getRole().equals(USER_ROLES.ADMIN.toString())) {
            throw new UserMngAppExceptionMessage("Cannot delete Admin User!");
        }
        userRepository.delete(id);
    }
}

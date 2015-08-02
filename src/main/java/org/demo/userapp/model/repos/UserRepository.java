package org.demo.userapp.model.repos;

import org.demo.userapp.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by k on 7/30/2015.
 */
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

}

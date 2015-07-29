package org.demo.westernacher.model.repos;

import org.demo.westernacher.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by k on 7/30/2015.
 */
public interface UserRepository extends JpaRepository<User,Long> {


}

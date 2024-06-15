package com.swarnim.userservice.repositories;

import com.swarnim.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Override
    User save(User user); //upsert - if user is already present in the db it will update or else it will create the new user

    Optional<User> findByEmail(String email);

}

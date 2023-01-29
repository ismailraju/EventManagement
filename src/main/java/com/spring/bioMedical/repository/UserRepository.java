package com.spring.bioMedical.repository;

import com.spring.bioMedical.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {


    User findByEmail(String email);

    User findByConfirmationToken(String confirmationToken);

    List<User> findAll();
}
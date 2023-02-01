package com.spring.eventmanagement.repository;

import com.spring.eventmanagement.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByEmail(String user);

    List<Admin> findByRole(String user);


    @Query("select count(b) from Admin b where b.email = ?1 and b.id != ?2 ")
    Long findByEmailAndIdNot(String email, Integer id);
}
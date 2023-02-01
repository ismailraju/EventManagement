package com.spring.eventmanagement.repository;

import com.spring.eventmanagement.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByEmail(String user);

    List<Admin> findByRole(String user);
}
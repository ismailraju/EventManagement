package com.spring.bioMedical.service;

import java.util.List;

import com.spring.bioMedical.entity.Admin;

public interface AdminService {


    Admin findById(Integer id);

    public List<Admin> findByRole(String user);

    public Admin findByEmail(String user);

    public List<Admin> findAll();

    public void save(Admin admin);

}

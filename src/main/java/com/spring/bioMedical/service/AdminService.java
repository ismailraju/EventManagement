package com.spring.bioMedical.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.repository.AdminRepository;


@Service
public class AdminService {

    private AdminRepository adminRepository;

    //inject employee dao
    @Autowired   //Adding bean id @Qualifier
    public AdminService(AdminRepository obj) {
        adminRepository = obj;
    }


    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public void save(Admin admin) {
        adminRepository.save(admin);
    }

    public Admin findByEmail(String user) {

        return adminRepository.findByEmail(user);

    }

    public Admin findById(Integer id) {

        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            return optionalAdmin.get();
        }
        return new Admin();
    }

    public List<Admin> findByRole(String user) {

        return adminRepository.findByRole(user);
    }


}

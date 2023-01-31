package com.spring.bioMedical.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.repository.AdminRepository;

/**
 * @author Soumyadip Chowdhury
 * @github soumyadip007
 */
@Service
public class AdminServiceImplementation implements AdminService {

    private AdminRepository adminRepository;

    //inject employee dao
    @Autowired   //Adding bean id @Qualifier
    public AdminServiceImplementation(AdminRepository obj) {
        adminRepository = obj;
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public void save(Admin admin) {

        adminRepository.save(admin);
    }

    @Override
    public Admin findByEmail(String user) {
        // TODO Auto-generated method stub

        return adminRepository.findByEmail(user);

    }

    @Override
    public Admin findById(Integer id) {

        Optional<Admin> optionalAdmin = adminRepository.findById(Long.valueOf(id));
        if (optionalAdmin.isPresent()) {
            return optionalAdmin.get();
        }
        return new Admin();
    }

    @Override
    public List<Admin> findByRole(String user) {

        return adminRepository.findByRole(user);
    }


}

package com.spring.bioMedical.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Soumyadip Chowdhury
 * @github soumyadip007
 *
 */
@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
	
	
	 User findByEmail(String email);
	
	 User findByConfirmationToken(String confirmationToken);
	 
	  List<User> findAll();
}
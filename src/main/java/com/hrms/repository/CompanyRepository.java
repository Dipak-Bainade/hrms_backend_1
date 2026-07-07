package com.hrms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	
	Optional<Company> findByEmail(String email);
	
	boolean existsByEmail(String email);

}

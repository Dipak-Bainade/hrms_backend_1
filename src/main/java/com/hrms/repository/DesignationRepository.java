package com.hrms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Designation;

public interface DesignationRepository extends JpaRepository<Designation, Long>{

	
	Optional<Designation> findByIdAndCompanyId(Long id, Long companyId);
	
}

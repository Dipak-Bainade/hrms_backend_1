package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{

	
    List<Department> findByCompanyId(Long companyId);

    Optional<Department> findByIdAndCompanyId(
            Long id,
            Long companyId);

    boolean existsByDepartmentNameAndCompanyId(
            String departmentName,
            Long companyId);
    
  
}

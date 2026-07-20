package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hrms.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	
	
	Page<Employee> findByCompanyId(
	        Long companyId,
	        Pageable pageable);
	
	List<Employee> findByCompanyId(Long companyId);
	

    Optional<Employee> findByIdAndCompanyId(
            Long id,
            Long companyId);

    boolean existsByEmailAndCompanyId(
            String email,
            Long companyId);

    boolean existsByEmployeeCodeAndCompanyId(
            String employeeCode,
            Long companyId);
    
    Optional<Employee> findTopByCompanyIdOrderByEmployeeCodeDesc(Long companyId);
    
    List<Employee> findByCompanyIdAndActiveTrue(Long companyId);
    
    
    @Query("""
    		SELECT e
    		FROM Employee e
    		WHERE e.company.id = :companyId
    		AND (
    		    LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
    		    OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
    		    OR LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%'))
    		    OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
    		)
    		""")
    		Page<Employee> searchEmployees(
    		        @Param("companyId") Long companyId,
    		        @Param("search") String search,
    		        Pageable pageable);
    
    Optional<Employee>

    findByUserIdAndCompanyId(

            Long userId,

            Long companyId);
    
}

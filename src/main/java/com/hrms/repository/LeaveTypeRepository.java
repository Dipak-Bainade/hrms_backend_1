package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.LeaveType;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long>{

    List<LeaveType> findByCompanyId(Long companyId);

    Optional<LeaveType> findByIdAndCompanyId(
            Long id,
            Long companyId);

    boolean existsByCompanyIdAndLeaveNameIgnoreCase(
            Long companyId,
            String leaveName);
    
    List<LeaveType> findByCompanyIdAndActiveTrue(Long companyId);
	
}

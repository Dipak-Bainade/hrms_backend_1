package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.LeaveBalance;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long>{
	

    List<LeaveBalance> findByEmployeeIdAndCompanyId(
            Long employeeId,
            Long companyId);

    Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeIdAndYear(
            Long employeeId,
            Long leaveTypeId,
            Integer year);
    
    boolean existsByEmployeeIdAndLeaveTypeIdAndYear(
            Long employeeId,
            Long leaveTypeId,
            Integer year);

}

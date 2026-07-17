package com.hrms.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hrms.entity.LeaveRequest;
import com.hrms.enums.LeaveStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>{

	
    List<LeaveRequest> findByEmployeeIdAndCompanyId(
            Long employeeId,
            Long companyId);
    
    @Query("""
    		SELECT COUNT(l) > 0
    		FROM LeaveRequest l
    		WHERE l.employee.id = :employeeId
    		AND l.company.id = :companyId
    		AND l.status <> com.hrms.enums.LeaveStatus.REJECTED
    		AND l.fromDate <= :toDate
    		AND l.toDate >= :fromDate
    		""")
    		boolean existsOverlappingLeave(

    		        Long employeeId,

    		        Long companyId,

    		        LocalDate fromDate,

    		        LocalDate toDate);
    
    
    Optional<LeaveRequest> findByIdAndCompanyId(
            Long id,
            Long companyId);
    
    Page<LeaveRequest> findByEmployeeIdAndCompanyId(
            Long employeeId,
            Long companyId,
            Pageable pageable);
    
    @Query("""
    		SELECT lr
    		FROM LeaveRequest lr
    		WHERE lr.employee.id = :employeeId
    		AND lr.company.id = :companyId
    		AND lr.fromDate >= :fromDate
    		AND lr.toDate <= :toDate
    		""")
    		Page<LeaveRequest> findByEmployeeAndDateRange(

    		        Long employeeId,

    		        Long companyId,

    		        LocalDate fromDate,

    		        LocalDate toDate,

    		        Pageable pageable);
 
    Page<LeaveRequest> findByEmployeeIdAndCompanyIdAndStatus(

            Long employeeId,

            Long companyId,

            LeaveStatus status,

            Pageable pageable);
    
    
    Page<LeaveRequest> findByCompanyIdAndStatus(

            Long companyId,

            LeaveStatus status,

            Pageable pageable);
    
    
    @Query("""
    		SELECT lr
    		FROM LeaveRequest lr
    		WHERE lr.company.id = :companyId
    		AND lr.status = :status
    		AND lr.employee.department.id = :departmentId
    		""")
    		Page<LeaveRequest> findPendingByDepartment(

    		        Long companyId,

    		        LeaveStatus status,

    		        Long departmentId,

    		        Pageable pageable);
    
    Page<LeaveRequest> findByCompanyIdAndStatusAndLeaveTypeId(

            Long companyId,

            LeaveStatus status,

            Long leaveTypeId,

            Pageable pageable);
    
    @Query("""
    		SELECT lr
    		FROM LeaveRequest lr
    		WHERE lr.company.id = :companyId
    		AND lr.status = :status
    		AND lr.fromDate <= :toDate
    		AND lr.toDate >= :fromDate
    		""")
    		Page<LeaveRequest> findPendingBetweenDates(

    		        Long companyId,

    		        LeaveStatus status,

    		        LocalDate fromDate,

    		        LocalDate toDate,

    		        Pageable pageable);
    
    long countByCompanyIdAndStatus(

            Long companyId,

            LeaveStatus status);
    
    long countByCompanyIdAndStatusAndAppliedAtBetween(

            Long companyId,

            LeaveStatus status,

            LocalDateTime start,

            LocalDateTime end);
    
    long countByCompanyIdAndStatusAndApprovedAtBetween(

            Long companyId,

            LeaveStatus status,

            LocalDateTime start,

            LocalDateTime end);
    
    @Query("""
    		SELECT COUNT(DISTINCT lr.employee.id)
    		FROM LeaveRequest lr
    		WHERE lr.company.id = :companyId
    		AND lr.status = 'APPROVED'
    		AND CURRENT_DATE BETWEEN lr.fromDate AND lr.toDate
    		""")
    		long countEmployeesCurrentlyOnLeave(
    		        Long companyId);
    
    
    
    
    
    
}

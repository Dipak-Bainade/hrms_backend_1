package com.hrms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

	

    Optional<Attendance> findByEmployeeIdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate);

    List<Attendance> findByCompanyIdAndAttendanceDate(
            Long companyId,
            LocalDate attendanceDate);

    List<Attendance> findByEmployeeId(Long employeeId);
    
    Optional<Employee> findByIdAndCompanyId(
            Long id,
            Long companyId);
    
    boolean existsByEmployeeIdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate);
    
    Optional<Attendance> findByEmployeeIdAndCompanyIdAndAttendanceDate(
            Long employeeId,
            Long companyId,
            LocalDate attendanceDate);
    
    Page<Attendance> findByEmployeeIdAndCompanyId(
            Long employeeId,
            Long companyId,
            Pageable pageable);
    
    @Query("""
    		SELECT a
    		FROM Attendance a
    		WHERE a.employee.id = :employeeId
    		AND a.company.id = :companyId
    		AND a.attendanceDate BETWEEN :fromDate AND :toDate
    		""")
    		Page<Attendance> findAttendanceBetweenDates(

    		        Long employeeId,

    		        Long companyId,

    		        LocalDate fromDate,

    		        LocalDate toDate,

    		        Pageable pageable);
   
    
    @Query("""
    		SELECT a
    		FROM Attendance a
    		WHERE a.company.id = :companyId
    		AND YEAR(a.attendanceDate) = :year
    		AND MONTH(a.attendanceDate) = :month
    		AND (:employeeId IS NULL OR a.employee.id = :employeeId)
    		ORDER BY a.attendanceDate
    		""")
    		List<Attendance> findMonthlyAttendance(

    		        Long companyId,

    		        Long employeeId,

    		        int month,

    		        int year);
    
}

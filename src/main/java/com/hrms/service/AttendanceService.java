package com.hrms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.hrms.dto.payroll.AttendanceSummary;
import com.hrms.dto.request.AttendanceRequest;
import com.hrms.dto.response.AttendanceResponse;
import com.hrms.dto.response.MonthlyAttendanceResponse;

public interface AttendanceService {
	
    AttendanceResponse checkIn(
            AttendanceRequest request);

    AttendanceResponse checkOut(
            Long employeeId);

    List<AttendanceResponse> getAttendanceByDate(
            LocalDate date);

    List<AttendanceResponse> getEmployeeAttendance(
            Long employeeId);
    
    Page<AttendanceResponse> getEmployeeAttendance(

            Long employeeId,

            LocalDate fromDate,

            LocalDate toDate,

            int page,

            int size,

            String sortBy,

            String direction);
    
    List<MonthlyAttendanceResponse> getMonthlyAttendance(

            Long employeeId,

            int month,

            int year);
    
    AttendanceSummary getAttendanceSummary(
            Long employeeId,
            Integer month,
            Integer year);

}

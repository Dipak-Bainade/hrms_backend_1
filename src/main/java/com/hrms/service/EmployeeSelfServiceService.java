package com.hrms.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hrms.dto.request.LeaveRequestRequest;
import com.hrms.dto.request.UpdateMyProfileRequest;
import com.hrms.dto.response.AttendanceResponse;
import com.hrms.dto.response.AttendanceSummaryResponse;
import com.hrms.dto.response.EmployeeAttendanceDashboardResponse;
import com.hrms.dto.response.EmployeeResponse;
import com.hrms.dto.response.LeaveDashboardResponse;
import com.hrms.dto.response.LeaveRequestResponse;
import com.hrms.dto.response.MyLeaveBalanceResponse;
import com.hrms.dto.response.PayslipDashboardResponse;
import com.hrms.dto.response.PayslipDetailResponse;
import com.hrms.dto.response.PayslipSummaryResponse;

public interface EmployeeSelfServiceService {

    EmployeeResponse getMyProfile();

    EmployeeResponse updateMyProfile(
            UpdateMyProfileRequest request);
    
    Page<AttendanceResponse> getMyAttendance(

            LocalDate fromDate,

            LocalDate toDate,

            Pageable pageable);

    AttendanceSummaryResponse getMonthlyAttendance(

            int month,

            int year);

    EmployeeAttendanceDashboardResponse getAttendanceDashboard(

            int month,

            int year);
    
    List<MyLeaveBalanceResponse> getMyLeaveBalances();
    
    Page<LeaveRequestResponse> getMyLeaveHistory(
            Pageable pageable);
    
    LeaveRequestResponse applyMyLeave(
            LeaveRequestRequest request);
    
    LeaveDashboardResponse getLeaveDashboard();
    
    LeaveRequestResponse cancelMyLeave(
            Long leaveRequestId);
    
    
    Page<PayslipSummaryResponse> getMyPayslips(

            Integer month,

            Integer year,

            Pageable pageable);

    PayslipDetailResponse getMyPayslip(

            Long payrollId);

    byte[] downloadMyPayslip(

            Long payrollId);

    PayslipDashboardResponse getPayrollDashboard();


   

}

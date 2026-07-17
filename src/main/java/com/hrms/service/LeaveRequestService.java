package com.hrms.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.hrms.dto.request.LeaveApprovalRequest;
import com.hrms.dto.request.LeaveRequestRequest;
import com.hrms.dto.response.LeaveDashboardResponse;
import com.hrms.dto.response.LeaveRequestResponse;
import com.hrms.enums.LeaveStatus;

public interface LeaveRequestService {


	
    LeaveRequestResponse applyLeave(
            LeaveRequestRequest request);
    
    LeaveRequestResponse approveLeave(
            Long leaveRequestId,
            LeaveApprovalRequest request);

    LeaveRequestResponse rejectLeave(
            Long leaveRequestId,
            LeaveApprovalRequest request);
    
    Page<LeaveRequestResponse> getEmployeeLeaveHistory(

            Long employeeId,

            LocalDate fromDate,

            LocalDate toDate,

            LeaveStatus status,

            int page,

            int size,

            String sortBy,

            String direction);
    
    Page<LeaveRequestResponse> getPendingRequests(

            Long departmentId,

            Long leaveTypeId,

            LocalDate fromDate,

            LocalDate toDate,

            int page,

            int size,

            String sortBy,

            String direction);

    LeaveDashboardResponse getDashboard();
    
    
    
    
}

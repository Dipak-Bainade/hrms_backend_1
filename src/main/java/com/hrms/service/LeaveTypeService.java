package com.hrms.service;

import java.util.List;

import com.hrms.dto.request.LeaveTypeRequest;
import com.hrms.dto.response.LeaveTypeResponse;

public interface LeaveTypeService {
	

    LeaveTypeResponse createLeaveType(
            LeaveTypeRequest request);

    List<LeaveTypeResponse> getAllLeaveTypes();

    LeaveTypeResponse getLeaveTypeById(Long id);

    LeaveTypeResponse updateLeaveType(
            Long id,
            LeaveTypeRequest request);

    void deleteLeaveType(Long id);

}

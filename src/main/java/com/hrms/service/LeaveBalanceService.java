package com.hrms.service;

import java.util.List;

import com.hrms.dto.response.LeaveBalanceResponse;

public interface LeaveBalanceService {
	
	 List<LeaveBalanceResponse> getEmployeeLeaveBalance(
	            Long employeeId);

}

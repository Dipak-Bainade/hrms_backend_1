package com.hrms.service;

import com.hrms.entity.Employee;
import com.hrms.entity.LeaveType;

public interface LeaveBalanceInitializerService {
	

    void initializeForEmployee(Employee employee);

    void initializeForLeaveType(LeaveType leaveType);

}

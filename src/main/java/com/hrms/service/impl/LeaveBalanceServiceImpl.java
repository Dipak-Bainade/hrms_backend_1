package com.hrms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.dto.response.LeaveBalanceResponse;
import com.hrms.entity.LeaveBalance;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveBalanceRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.LeaveBalanceService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl
        implements LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;

    private final EmployeeRepository employeeRepository;

    private final CurrentUser currentUser;

    @Override
    public List<LeaveBalanceResponse> getEmployeeLeaveBalance(
            Long employeeId) {

        Long companyId = currentUser.getCompanyId();

        employeeRepository
                .findByIdAndCompanyId(employeeId, companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found."));

        return leaveBalanceRepository

                .findByEmployeeIdAndCompanyId(
                        employeeId,
                        companyId)

                .stream()

                .map(this::mapToResponse)

                .toList();

    }

    private LeaveBalanceResponse mapToResponse(
            LeaveBalance balance) {

        return LeaveBalanceResponse.builder()

                .leaveTypeId(balance.getLeaveType().getId())

                .leaveType(balance.getLeaveType().getLeaveName())

                .allocatedLeaves(balance.getAllocatedLeaves())

                .usedLeaves(balance.getUsedLeaves())

                .remainingLeaves(balance.getRemainingLeaves())

                .year(balance.getYear())

                .build();

    }

}

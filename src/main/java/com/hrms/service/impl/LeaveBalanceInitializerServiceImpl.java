package com.hrms.service.impl;

import java.time.Year;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.entity.Employee;
import com.hrms.entity.LeaveBalance;
import com.hrms.entity.LeaveType;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveBalanceRepository;
import com.hrms.repository.LeaveTypeRepository;
import com.hrms.service.LeaveBalanceInitializerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveBalanceInitializerServiceImpl
        implements LeaveBalanceInitializerService {

    private final LeaveBalanceRepository leaveBalanceRepository;

    private final LeaveTypeRepository leaveTypeRepository;

    private final EmployeeRepository employeeRepository;

    @Override
    public void initializeForEmployee(Employee employee) {

        List<LeaveType> leaveTypes =
        		leaveTypeRepository.findByCompanyIdAndActiveTrue(
        		        employee.getCompany().getId());

        for (LeaveType leaveType : leaveTypes) {

            createBalance(employee, leaveType);

        }

    }

    @Override
    public void initializeForLeaveType(LeaveType leaveType) {

        List<Employee> employees =
        		employeeRepository.findByCompanyIdAndActiveTrue(
        		        leaveType.getCompany().getId());

        for (Employee employee : employees) {

            createBalance(employee, leaveType);

        }

    }

    private void createBalance(
            Employee employee,
            LeaveType leaveType) {

        Integer year = Year.now().getValue();

        boolean exists =
                leaveBalanceRepository
                        .existsByEmployeeIdAndLeaveTypeIdAndYear(

                                employee.getId(),

                                leaveType.getId(),

                                year);

        if (exists) {

            return;

        }

        LeaveBalance balance = new LeaveBalance();

        balance.setEmployee(employee);

        balance.setCompany(employee.getCompany());

        balance.setLeaveType(leaveType);

        balance.setYear(year);

        balance.setAllocatedLeaves(
                leaveType.getDefaultDays().doubleValue());

        balance.setUsedLeaves(0.0);

        balance.setRemainingLeaves(
                leaveType.getDefaultDays().doubleValue());

        balance.setActive(true);

        leaveBalanceRepository.save(balance);

    }

}

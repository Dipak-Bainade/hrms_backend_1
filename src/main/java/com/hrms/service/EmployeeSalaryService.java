package com.hrms.service;

import java.util.List;

import com.hrms.dto.request.EmployeeSalaryRequest;
import com.hrms.dto.response.EmployeeSalaryResponse;

public interface EmployeeSalaryService {

    EmployeeSalaryResponse assignSalary(
            EmployeeSalaryRequest request);

    EmployeeSalaryResponse getCurrentSalary(
            Long employeeId);

    List<EmployeeSalaryResponse>
    getSalaryHistory(
            Long employeeId);

}
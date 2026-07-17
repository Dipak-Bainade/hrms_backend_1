package com.hrms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.request.EmployeeSalaryRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.dto.response.EmployeeSalaryResponse;
import com.hrms.service.EmployeeSalaryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee-salaries")
@RequiredArgsConstructor
public class EmployeeSalaryController {

    private final EmployeeSalaryService service;

    @PostMapping
    @PreAuthorize("hasAuthority('PAYROLL_ASSIGN')")
    public ResponseEntity<ApiResponse<EmployeeSalaryResponse>>
    assignSalary(
            @Valid
            @RequestBody
            EmployeeSalaryRequest request) {

        return ResponseEntity.ok(

                ApiResponse
                        .<EmployeeSalaryResponse>builder()
                        .success(true)
                        .message("Salary assigned successfully.")
                        .data(service.assignSalary(request))
                        .build());

    }

}

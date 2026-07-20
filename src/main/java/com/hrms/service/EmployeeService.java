package com.hrms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.hrms.dto.request.EmployeeRequest;
import com.hrms.dto.request.UpdateMyProfileRequest;
import com.hrms.dto.response.EmployeeResponse;


public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deactivateEmployee(Long id);
    
    Page<EmployeeResponse> getEmployees(

            String search,

            int page,

            int size,

            String sortBy,

            String direction);
    
    void updateEmployeeStatus(Long id, boolean active);
    
    EmployeeResponse updateMyProfile(
            UpdateMyProfileRequest request);
    
    EmployeeResponse getMyProfile();
}

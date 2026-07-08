package com.hrms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hrms.dto.request.DepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;
import com.hrms.entity.Department;
import com.hrms.repository.DepartmentRepository;
import com.hrms.security.CurrentUser;

@Service
public interface DepartmentService {


    DepartmentResponse createDepartment(
            DepartmentRequest request);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse getDepartmentById(Long id);

    DepartmentResponse updateDepartment(
            Long id,
            DepartmentRequest request);

    void deleteDepartment(Long id);
	
}

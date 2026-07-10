package com.hrms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hrms.dto.request.DepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;
import com.hrms.entity.Company;
import com.hrms.entity.Department;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.CompanyRepository;
import com.hrms.repository.DepartmentRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.DepartmentService;

import com.hrms.service.DepartmentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
	
    private final DepartmentRepository departmentRepository;

    private final CompanyRepository companyRepository;

    private final CurrentUser currentUser;
    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository,
            CompanyRepository companyRepository,
            CurrentUser currentUser) {

        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
        this.currentUser = currentUser;
    }

	@Override
	public DepartmentResponse createDepartment(DepartmentRequest request) {
		Long companyId = currentUser.getCompanyId();

	    if (departmentRepository.existsByDepartmentNameAndCompanyId(
	            request.getDepartmentName(),
	            companyId)) {

	    	throw new DuplicateResourceException(
	    	        "Department already exists.");
	    }

	    Company company = companyRepository.findById(companyId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Company not found."));

	    Department department = new Department();

	    department.setDepartmentName(request.getDepartmentName());
	    department.setDescription(request.getDescription());
	    department.setCompany(company);

	    department = departmentRepository.save(department);

	    return new DepartmentResponse(
	            department.getId(),
	            department.getDepartmentName(),
	            department.getDescription(),
	            company.getId());
	}

	@Override
	public List<DepartmentResponse> getAllDepartments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DepartmentResponse getDepartmentById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDepartment(Long id) {
		// TODO Auto-generated method stub
		
	}
	

}

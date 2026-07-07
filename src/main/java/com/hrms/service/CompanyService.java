package com.hrms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hrms.dto.request.CompanyRegistrationRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.entity.Company;

public interface CompanyService {
	
//	Company registerCompany(CompanyRegistrationRequest request);

    List<Company> getAllCompanies();

    Company getCompanyById(Long id);

    Company updateCompany(Long id, Company company);
    
    ApiResponse registerCompany(CompanyRegistrationRequest request);

}

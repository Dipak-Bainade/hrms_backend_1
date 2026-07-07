package com.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.request.CompanyRegistrationRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.entity.Company;
import com.hrms.service.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {
	
	@Autowired
    private CompanyService companyService;
	
	   public CompanyController(CompanyService companyService) {
	        this.companyService = companyService;
	    }

    // Register Company
	@PostMapping("/register")
	public ResponseEntity<?> registerCompany(
	        @Valid @RequestBody CompanyRegistrationRequest request) {

	    try {

	        ApiResponse response = companyService.registerCompany(request);

	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (RuntimeException ex) {

	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(false, ex.getMessage()));

	    }
	}

    // Get All Companies
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {

        List<Company> companies = companyService.getAllCompanies();

        return ResponseEntity.ok(companies);
    }

    // Get Company By Id
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {

        Company company = companyService.getCompanyById(id);

        return ResponseEntity.ok(company);
    }

    // Update Company
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable Long id,
            @RequestBody Company company) {

        Company updatedCompany = companyService.updateCompany(id, company);

        return ResponseEntity.ok(updatedCompany);
    }

}

package com.hrms.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hrms.dto.request.CompanyRegistrationRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.entity.Company;
import com.hrms.entity.Role;
import com.hrms.entity.User;
import com.hrms.repository.CompanyRepository;
import com.hrms.repository.RoleRepository;
import com.hrms.repository.UserRepository;
import com.hrms.service.CompanyService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService{
	
	private final CompanyRepository companyRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public CompanyServiceImpl(
	        CompanyRepository companyRepository,
	        UserRepository userRepository,
	        RoleRepository roleRepository,
	        BCryptPasswordEncoder passwordEncoder) {

	    this.companyRepository = companyRepository;
	    this.userRepository = userRepository;
	    this.roleRepository = roleRepository;
	    this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<Company> getAllCompanies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Company getCompanyById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Company updateCompany(Long id, Company company) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse registerCompany(CompanyRegistrationRequest request) {
		// TODO Auto-generated method stub
		  if (companyRepository.existsByEmail(request.getCompanyEmail())) {
	            throw new RuntimeException("Company email already exists.");
	        }
		  
		  if (userRepository.existsByEmail(request.getAdminEmail())) {
			    throw new RuntimeException("Admin email already exists.");
			}
		  
		    Company company = new Company();

		    company.setCompanyName(request.getCompanyName());
		    company.setEmail(request.getCompanyEmail());
		    company.setPhone(request.getCompanyPhone());
		    company.setAddress(request.getAddress());
		    company.setCity(request.getCity());
		    company.setState(request.getState());
		    company.setCountry(request.getCountry());
		    company.setPinCode(request.getPinCode());
		    company.setWebsite(request.getWebsite());
		    company.setSubscriptionType("FREE");
		    company.setSubscriptionExpiry(LocalDate.now().plusDays(30));
		    company = companyRepository.save(company);
		    
		    Role role = roleRepository.findByRoleName("COMPANY_ADMIN")
		            .orElseThrow(() -> new RuntimeException("Role not found"));
		    
		    User user = new User();

		    user.setFirstName(request.getAdminFirstName());
		    user.setLastName(request.getAdminLastName());
		    user.setEmail(request.getAdminEmail());
		    user.setMobile(request.getAdminMobile());

		    user.setPassword(
		            passwordEncoder.encode(request.getPassword())
		    );

		    user.setCompany(company);
		    user.setRole(role);
		    
		    userRepository.save(user);
		    
		    return new ApiResponse(
		            true,
		            "Company registered successfully."
		    );
		    
	}

}

package com.hrms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hrms.dto.request.EmployeeRequest;
import com.hrms.dto.response.EmployeeResponse;
import com.hrms.entity.Company;
import com.hrms.entity.Department;
import com.hrms.entity.Designation;
import com.hrms.entity.Employee;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.CompanyRepository;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.DesignationRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.EmployeeService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

	    
	    private final EmployeeRepository employeeRepository;
	    private final DepartmentRepository departmentRepository;
	    private final DesignationRepository designationRepository;
	    private final CompanyRepository companyRepository;
	    private final CurrentUser currentUser;
	    
	    @Override
	    public EmployeeResponse createEmployee(EmployeeRequest request) {

	        Long companyId = currentUser.getCompanyId();

	        validateDuplicateEmail(request.getEmail(), companyId);

	        Department department = validateDepartment(
	                request.getDepartmentId(),
	                companyId);

	        Designation designation = validateDesignation(
	                request.getDesignationId(),
	                companyId);

	        Company company = companyRepository.findById(companyId)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Company not found."));

	        String employeeCode = generateEmployeeCode(companyId);

	        Employee employee = mapToEmployee(
	                request,
	                employeeCode,
	                company,
	                department,
	                designation);

	        employee = employeeRepository.save(employee);

	        return mapToResponse(employee);
	    }
	    
	    @Override
	    @Transactional(readOnly = true)
	    public EmployeeResponse getEmployeeById(Long id) {

	        Long companyId = currentUser.getCompanyId();

	        Employee employee = employeeRepository
	                .findByIdAndCompanyId(id, companyId)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Employee not found."));

	        return mapToResponse(employee);
	    }

	    @Override
	    public List<EmployeeResponse> getAllEmployees() {
	        throw new UnsupportedOperationException("Not implemented yet");
	    }

	    
	    @Override
	    public EmployeeResponse updateEmployee(
	            Long id,
	            EmployeeRequest request) {

	        Long companyId = currentUser.getCompanyId();

	        Employee employee = employeeRepository
	                .findByIdAndCompanyId(id, companyId)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Employee not found."));

	        Department department = departmentRepository
	                .findByIdAndCompanyId(
	                        request.getDepartmentId(),
	                        companyId)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Department not found."));

	        Designation designation = designationRepository
	                .findByIdAndCompanyId(
	                        request.getDesignationId(),
	                        companyId)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Designation not found."));

	        employee.setFirstName(request.getFirstName());
	        employee.setLastName(request.getLastName());
	        employee.setEmail(request.getEmail());
	        employee.setMobile(request.getMobile());
	        employee.setDateOfBirth(request.getDateOfBirth());
	        employee.setGender(request.getGender());
	        employee.setJoiningDate(request.getJoiningDate());
	        employee.setDepartment(department);
	        employee.setDesignation(designation);

	        employeeRepository.save(employee);

	        return mapToResponse(employee);
	    }
	  

	    @Override
	    public void deactivateEmployee(Long id) {
	        throw new UnsupportedOperationException("Not implemented yet");
	    }
	    
	    private void validateDuplicateEmail(String email, Long companyId) {

	        if (employeeRepository.existsByEmailAndCompanyId(email, companyId)) {
	            throw new RuntimeException("Employee email already exists.");
	        }
	    }
	    
	    private Department validateDepartment(Long departmentId, Long companyId) {

	        return departmentRepository.findByIdAndCompanyId(departmentId, companyId)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Department not found."));
	    }
	    
	    private Designation validateDesignation(Long designationId, Long companyId) {

	        return designationRepository.findByIdAndCompanyId(designationId, companyId)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Designation not found."));
	    }
	    
	    private String generateEmployeeCode(Long companyId) {

	        Employee lastEmployee = employeeRepository
	                .findTopByCompanyIdOrderByEmployeeCodeDesc(companyId)
	                .orElse(null);

	        if (lastEmployee == null) {
	            return "EMP000001";
	        }

	        String lastCode = lastEmployee.getEmployeeCode();

	        int number = Integer.parseInt(lastCode.substring(3));

	        return String.format("EMP%06d", number + 1);
	    }
	    
	    private Employee mapToEmployee(
	            EmployeeRequest request,
	            String employeeCode,
	            Company company,
	            Department department,
	            Designation designation) {

	        Employee employee = new Employee();

	        employee.setEmployeeCode(employeeCode);
	        employee.setFirstName(request.getFirstName());
	        employee.setLastName(request.getLastName());
	        employee.setEmail(request.getEmail());
	        employee.setMobile(request.getMobile());
	        employee.setDateOfBirth(request.getDateOfBirth());
	        employee.setGender(request.getGender());
	        employee.setJoiningDate(request.getJoiningDate());

	        employee.setCompany(company);
	        employee.setDepartment(department);
	        employee.setDesignation(designation);

	        employee.setActive(true);

	        return employee;
	    }
	    
	    private EmployeeResponse mapToResponse(Employee employee) {

	        return new EmployeeResponse(
	                employee.getId(),
	                employee.getEmployeeCode(),
	                employee.getFirstName(),
	                employee.getLastName(),
	                employee.getEmail(),
	                employee.getMobile(),
	                employee.getDateOfBirth(),
	                employee.getGender(),
	                employee.getJoiningDate(),
	                employee.getDepartment().getDepartmentName(),
	                employee.getDesignation().getDesignationName(),
	                employee.getActive()
	        );
	    }
	    
	    @Override
	    public Page<EmployeeResponse> getEmployees(

	            String search,

	            int page,

	            int size,

	            String sortBy,

	            String direction) {

	        Pageable pageable =
	                PageRequest.of(

	                        page,

	                        size,

	                        direction.equalsIgnoreCase("desc")

	                                ? Sort.by(sortBy).descending()

	                                : Sort.by(sortBy).ascending());

	        Long companyId =
	                currentUser.getCompanyId();

	        Page<Employee> employees;

	        if (search == null || search.isBlank()) {

	            employees =
	                    employeeRepository.findByCompanyId(

	                            companyId,

	                            pageable);

	        } else {

	            employees =
	                    employeeRepository.searchEmployees(

	                            companyId,

	                            search,

	                            pageable);

	        }

	        return employees.map(this::mapToResponse);

	    }
	    
	    @Override
	    public void updateEmployeeStatus(
	            Long id,
	            boolean active) {

	        Long companyId = currentUser.getCompanyId();

	        Employee employee = employeeRepository
	                .findByIdAndCompanyId(id, companyId)
	                .orElseThrow(() ->
	                        new ResourceNotFoundException("Employee not found."));

	        employee.setActive(active);

	        employeeRepository.save(employee);
	    }
	    
}

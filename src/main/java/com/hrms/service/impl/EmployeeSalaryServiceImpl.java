package com.hrms.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.dto.request.EmployeeSalaryRequest;
import com.hrms.dto.response.EmployeeSalaryResponse;
import com.hrms.entity.Employee;
import com.hrms.entity.EmployeeSalary;
import com.hrms.entity.SalaryStructure;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.EmployeeSalaryRepository;
import com.hrms.repository.SalaryStructureRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.EmployeeSalaryService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeSalaryServiceImpl  implements EmployeeSalaryService{
	
	private final EmployeeSalaryRepository employeeSalaryRepository;

	private final EmployeeRepository employeeRepository;

	private final SalaryStructureRepository salaryStructureRepository;

	private final CurrentUser currentUser;
	
	
	@Override
	@Transactional
	public EmployeeSalaryResponse assignSalary(
	        EmployeeSalaryRequest request) {

	    Long companyId =
	            currentUser.getCompanyId();

	    Employee employee =
	            employeeRepository
	                    .findByIdAndCompanyId(
	                            request.getEmployeeId(),
	                            companyId)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Employee not found"));

	    SalaryStructure structure =
	            salaryStructureRepository
	                    .findByIdAndCompanyId(
	                            request.getSalaryStructureId(),
	                            companyId)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Salary structure not found"));

	    deactivateCurrentSalary(
	            employee.getId(),
	            companyId,
	            request.getEffectiveFrom());

	    EmployeeSalary salary =
	            new EmployeeSalary();

	    salary.setEmployee(employee);

	    salary.setSalaryStructure(structure);

	    salary.setCompany(employee.getCompany());

	    salary.setEffectiveFrom(
	            request.getEffectiveFrom());

	    salary.setActive(true);

	    salary = employeeSalaryRepository
	            .save(salary);

	    return mapToResponse(salary);
	}
	
	private void deactivateCurrentSalary(

	        Long employeeId,

	        Long companyId,

	        LocalDate newEffectiveFrom) {

	    employeeSalaryRepository

	            .findByEmployeeIdAndCompanyIdAndActiveTrue(
	                    employeeId,
	                    companyId)

	            .ifPresent(existing -> {

	                existing.setActive(false);

	                existing.setEffectiveTo(
	                        newEffectiveFrom.minusDays(1));

	                employeeSalaryRepository
	                        .save(existing);

	            });

	}
	
	
	@Override
	@Transactional(readOnly = true)
	public EmployeeSalaryResponse getCurrentSalary(
	        Long employeeId) {

	    Long companyId =
	            currentUser.getCompanyId();

	    EmployeeSalary salary =
	            employeeSalaryRepository
	                    .findByEmployeeIdAndCompanyIdAndActiveTrue(
	                            employeeId,
	                            companyId)

	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Salary assignment not found"));

	    return mapToResponse(salary);

	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EmployeeSalaryResponse>
	getSalaryHistory(Long employeeId) {

	    Long companyId =
	            currentUser.getCompanyId();

	    return employeeSalaryRepository

	            .findByEmployeeIdAndCompanyIdOrderByEffectiveFromDesc(
	                    employeeId,
	                    companyId)

	            .stream()

	            .map(this::mapToResponse)

	            .toList();

	}
	
	private EmployeeSalaryResponse mapToResponse(
	        EmployeeSalary salary) {

	    SalaryStructure structure = salary.getSalaryStructure();

	    return EmployeeSalaryResponse.builder()
	            .id(salary.getId())
	            .employeeId(salary.getEmployee().getId())
	            .employeeCode(salary.getEmployee().getEmployeeCode())
	            .employeeName(
	                    salary.getEmployee().getFirstName()
	                    + " "
	                    + salary.getEmployee().getLastName())
	            .salaryStructureId(structure.getId())
	            .salaryStructureName(structure.getStructureName())
	            .grossSalary(structure.getGrossSalary())
	            .effectiveFrom(salary.getEffectiveFrom())
	            .effectiveTo(salary.getEffectiveTo())
	            .active(salary.getActive())
	            .build();
	}
	

}

package com.hrms.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.entity.Employee;
import com.hrms.service.EmployeeService;


@RestController
@RequestMapping("/test")
public class EmployeeController {
	
	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
	    this.employeeService = employeeService;
	}
	
	@GetMapping("/hello")
	public String hello() {

	    return "Welcome HRMS2";
	}
	
	
	
	
	@PostMapping
	@PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
	public Employee createEmployee(@RequestBody Employee employee) {

	    return employeeService.createEmployee(employee);
	}

	@GetMapping()
	@PreAuthorize("hasAuthority('EMPLOYEE_VIEW')")
	public List<Employee> getEmployees() {
	    return employeeService.getAllEmployees();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
	public Employee updateEmployee(
	        @PathVariable Long id,
	        @RequestBody Employee employee) {

	    return employeeService.updateEmployee(id, employee);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('EMPLOYEE_DELETE')")
	public void deleteEmployee(@PathVariable Long id) {

	    employeeService.deleteEmployee(id);
	}
	
}

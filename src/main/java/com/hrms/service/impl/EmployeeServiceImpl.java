package com.hrms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hrms.entity.Employee;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

	   private final EmployeeRepository employeeRepository;

	    @Override
	    public Employee createEmployee(Employee employee) {
	        return employeeRepository.save(employee);
	    }

	    @Override
	    public List<Employee> getAllEmployees() {
	        return employeeRepository.findAll();
	    }

	    @Override
	    public Employee updateEmployee(Long id, Employee employee) {

	        Employee existing = employeeRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Employee not found"));

	        // update fields

	        return employeeRepository.save(existing);
	    }

	    @Override
	    public void deleteEmployee(Long id) {
	        employeeRepository.deleteById(id);
	    }
}

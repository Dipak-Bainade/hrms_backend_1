package com.hrms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hrms.entity.Employee;


public interface EmployeeService {

    Employee createEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);
}

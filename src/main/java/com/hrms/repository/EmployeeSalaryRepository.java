package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.EmployeeSalary;

public interface EmployeeSalaryRepository
extends JpaRepository<EmployeeSalary, Long> {

Optional<EmployeeSalary> findByEmployeeIdAndActiveTrue(
    Long employeeId);

List<EmployeeSalary> findByEmployeeIdOrderByEffectiveFromDesc(
    Long employeeId);

Optional<EmployeeSalary>
findByEmployeeIdAndCompanyIdAndActiveTrue(
        Long employeeId,
        Long companyId);

List<EmployeeSalary>
findByEmployeeIdAndCompanyIdOrderByEffectiveFromDesc(
        Long employeeId,
        Long companyId);

}
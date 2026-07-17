package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hrms.entity.Payroll;

public interface PayrollRepository
extends JpaRepository<Payroll, Long> {

Optional<Payroll>

findByEmployeeIdAndCompanyIdAndPayMonthAndPayYear(

    Long employeeId,

    Long companyId,

    Integer month,

    Integer year);

List<Payroll>

findByEmployeeIdAndCompanyIdOrderByPayYearDescPayMonthDesc(

    Long employeeId,

    Long companyId);

Optional<Payroll> findByIdAndCompanyId(
        Long id,
        Long companyId);







Long countByCompanyIdAndPayMonthAndPayYear(
        Long companyId,
        Integer month,
        Integer year);

Long countByCompanyIdAndPayMonthAndPayYearAndProcessedFalse(
        Long companyId,
        Integer month,
        Integer year);


@Query("""
SELECT COALESCE(SUM(p.grossSalary), 0)
FROM Payroll p
WHERE p.company.id = :companyId
AND p.payMonth = :month
AND p.payYear = :year
""")
Double sumGrossSalary(Long companyId, Integer month, Integer year);

@Query("""
SELECT COALESCE(SUM(p.totalDeduction), 0)
FROM Payroll p
WHERE p.company.id = :companyId
AND p.payMonth = :month
AND p.payYear = :year
""")
Double sumTotalDeduction(Long companyId, Integer month, Integer year);

@Query("""
SELECT COALESCE(SUM(p.netSalary), 0)
FROM Payroll p
WHERE p.company.id = :companyId
AND p.payMonth = :month
AND p.payYear = :year
""")
Double sumNetSalary(Long companyId, Integer month, Integer year);

@Query("""
SELECT COUNT(p)
FROM Payroll p
WHERE p.company.id = :companyId
AND p.payMonth = :month
AND p.payYear = :year
""")
Long countEmployees(Long companyId, Integer month, Integer year);

}

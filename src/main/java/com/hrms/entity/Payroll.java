package com.hrms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "payrolls",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_employee_pay_month",
            columnNames = {
                "employee_id",
                "pay_month",
                "pay_year"
            }
        )
    }
)
@Getter
@Setter
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", nullable=false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id", nullable=false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_salary_id", nullable=false)
    private EmployeeSalary employeeSalary;

    @Column(nullable=false)
    private Integer payMonth;

    @Column(nullable=false)
    private Integer payYear;

    // Attendance Summary
    private Integer workingDays;

    private Integer presentDays;

    private Integer leaveDays;

    private Integer lopDays;

    private Double overtimeHours;

    // Salary

    private Double grossSalary;

    private Double totalAllowance;

    private Double totalDeduction;

    private Double overtimeAmount;

    private Double lopDeduction;

    private Double netSalary;

    @Column(nullable=false)
    private Boolean processed = true;

    private LocalDateTime processedAt;

}

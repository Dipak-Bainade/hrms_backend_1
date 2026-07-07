package com.hrms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="employees")
public class Employee extends BaseEntity{
	
	@NotBlank(message = "Employee code is required")
    @Column(nullable = false, unique = true, length = 20)
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(length = 10)
    private String gender;

    private LocalDate dob;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String mobile;

    private LocalDate joiningDate;

    @Column(precision = 12, scale = 2)
    private BigDecimal salary;

    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

}

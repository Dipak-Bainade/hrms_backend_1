package com.hrms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "salary_structures")
@Getter
@Setter
public class SalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String structureName;

    @Column(nullable = false)
    private Double basicSalary;

    private Double hra;

    private Double da;

    private Double specialAllowance;

    private Double otherAllowance;

    private Double pf;

    private Double professionalTax;

    private Double otherDeduction;

    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(nullable = false)
    private Double grossSalary;
    
    @Column
    private Double incomeTax;

}
package com.hrms.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayslipDetailResponse {

    private Long payrollId;

    private String employeeCode;

    private String employeeName;

    private String department;

    private String designation;

    private Integer month;

    private Integer year;

    private BigDecimal grossSalary;

    private BigDecimal allowances;

    private BigDecimal deductions;

    private BigDecimal netSalary;

    private BigDecimal overtimeAmount;

    private BigDecimal lopDeduction;

}

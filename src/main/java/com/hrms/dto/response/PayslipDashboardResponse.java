package com.hrms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class PayslipDashboardResponse {

    private BigDecimal latestNetSalary;

    private LocalDate latestPayrollDate;

    private BigDecimal yearToDateEarnings;

    private BigDecimal yearToDateDeductions;
    
    private Integer processedPayrolls;



}
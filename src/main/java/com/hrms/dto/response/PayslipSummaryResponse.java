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
public class PayslipSummaryResponse {

    private Long payrollId;

    private Integer month;

    private Integer year;

    private BigDecimal grossSalary;

    private BigDecimal deductions;

    private BigDecimal netSalary;

    private LocalDate processedDate;

}

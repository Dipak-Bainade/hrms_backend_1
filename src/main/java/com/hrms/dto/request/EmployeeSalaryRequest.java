package com.hrms.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeSalaryRequest {

    @NotNull
    private Long employeeId;

    @NotNull
    private Long salaryStructureId;

    @NotNull
    private LocalDate effectiveFrom;

}

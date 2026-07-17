package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryStructureRequest {

    @NotBlank
    private String structureName;

    @NotNull
    private Double basicSalary;

    private Double hra;

    private Double da;

    private Double specialAllowance;

    private Double otherAllowance;

    private Double pf;

    private Double professionalTax;

    private Double otherDeduction;

}

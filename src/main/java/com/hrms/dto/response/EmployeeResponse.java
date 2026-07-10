package com.hrms.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeResponse {
	
    private Long id;

    private String employeeCode;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private LocalDate dateOfBirth;

    private String gender;

    private LocalDate joiningDate;

    private String departmentName;

    private String designationName;

    private Boolean active;

}

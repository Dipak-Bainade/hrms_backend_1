package com.hrms.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeProfileResponse {
	
	    private Long id;

	    private String employeeCode;

	    private String fullName;

	    private String email;

	    private String mobile;

	    private String department;

	    private String designation;

	    private LocalDate joiningDate;

	    private LocalDate dateOfBirth;

	    private String gender;

	    private Boolean active;

}

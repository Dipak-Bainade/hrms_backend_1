package com.hrms.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
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
    
    private String address;
    
    private String city;
    
    private String state;
    
    private String pinCode;

}

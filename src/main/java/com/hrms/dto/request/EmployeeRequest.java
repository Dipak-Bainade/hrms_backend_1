package com.hrms.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequest {
	
	    @NotBlank(message = "First name is required")
	    private String firstName;

	    private String lastName;

	    @Email(message = "Invalid email")
	    @NotBlank(message = "Email is required")
	    private String email;

	    @NotBlank(message = "Mobile number is required")
	    private String mobile;

	    @NotNull(message = "Date of birth is required")
	    private LocalDate dateOfBirth;

	    @NotBlank(message = "Gender is required")
	    private String gender;

	    @NotNull(message = "Joining date is required")
	    private LocalDate joiningDate;

	    @NotNull(message = "Department is required")
	    private Long departmentId;

	    @NotNull(message = "Designation is required")
	    private Long designationId;

}

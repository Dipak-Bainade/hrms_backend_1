package com.hrms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegistrationRequest {
	
	    @NotBlank
	    private String companyName;

	    @NotBlank
	    @Email
	    private String companyEmail;

	    @NotBlank
	    private String companyPhone;

	    private String address;

	    private String city;

	    private String state;

	    private String country;

	    private String pinCode;

	    private String website;

	    // Admin Details
	    @NotBlank
	    private String adminFirstName;

	    @NotBlank
	    private String adminLastName;

	    @NotBlank
	    @Email
	    private String adminEmail;

	    @NotBlank
	    private String adminMobile;

	    @NotBlank
	    private String password;

}

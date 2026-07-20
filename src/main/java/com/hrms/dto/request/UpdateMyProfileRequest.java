package com.hrms.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMyProfileRequest {
	
	
    private String mobile;

    private String address;

    private String city;

    private String state;

    private String pinCode;

    private String emergencyContact;

    private String profilePhoto;

}

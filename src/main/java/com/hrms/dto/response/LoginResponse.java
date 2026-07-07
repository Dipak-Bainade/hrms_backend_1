package com.hrms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
	
    private String token;

    private String type;

    private Long userId;

    private String name;

    private String email;

    private String role;
    
    private Long companyId;

}

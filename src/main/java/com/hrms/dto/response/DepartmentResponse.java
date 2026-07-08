package com.hrms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentResponse {
	
	
    private Long id;

    private String departmentName;

    private String description;

    private Long companyId;

}

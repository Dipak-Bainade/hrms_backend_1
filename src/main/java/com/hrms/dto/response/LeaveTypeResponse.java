package com.hrms.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LeaveTypeResponse {
	
    private Long id;

    private String leaveName;

    private String description;

    private Integer defaultDays;

    private Boolean paidLeave;

    private Boolean active;

}

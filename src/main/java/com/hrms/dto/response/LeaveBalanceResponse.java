package com.hrms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalanceResponse {
	

    private Long leaveTypeId;

    private String leaveType;

    private Double allocatedLeaves;

    private Double usedLeaves;

    private Double remainingLeaves;

    private Integer year;

}

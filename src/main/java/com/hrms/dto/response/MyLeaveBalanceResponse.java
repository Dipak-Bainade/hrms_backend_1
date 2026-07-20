package com.hrms.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyLeaveBalanceResponse {

    private String leaveType;

    private Integer allocatedDays;

    private Integer usedDays;

    private Integer remainingDays;

}
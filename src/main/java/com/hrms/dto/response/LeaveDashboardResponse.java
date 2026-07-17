package com.hrms.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LeaveDashboardResponse {

    private long totalPending;

    private long pendingToday;

    private long pendingThisWeek;

    private long approvedThisMonth;

    private long rejectedThisMonth;

    private long employeesCurrentlyOnLeave;

}
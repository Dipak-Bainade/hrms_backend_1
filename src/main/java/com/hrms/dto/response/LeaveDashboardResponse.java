package com.hrms.dto.response;

import java.util.List;

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
    
    private Long pending;

    private Long approved;

    private Long rejected;

    private Long cancelled;

    private Long upcomingLeaves;

    private List<MyLeaveBalanceResponse> balances;

}
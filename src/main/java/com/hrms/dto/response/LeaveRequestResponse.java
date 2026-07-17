package com.hrms.dto.response;

import java.time.LocalDate;

import com.hrms.enums.LeaveStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveRequestResponse {
	
    private Long id;

    private Long employeeId;

    private String employeeCode;

    private String employeeName;

    private String leaveType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Double totalDays;

    private String reason;

    private LeaveStatus status;

}

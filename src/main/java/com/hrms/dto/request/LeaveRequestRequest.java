package com.hrms.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LeaveRequestRequest {
	
    @NotNull
    private Long employeeId;

    @NotNull
    private Long leaveTypeId;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    private String reason;

}

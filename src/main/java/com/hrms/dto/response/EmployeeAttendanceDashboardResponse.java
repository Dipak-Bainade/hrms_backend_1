package com.hrms.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeAttendanceDashboardResponse {

    private AttendanceSummaryResponse summary;

    private List<AttendanceResponse> recentAttendance;

}

package com.hrms.controller;

import java.time.LocalDate;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.request.EmployeeRequest;
import com.hrms.dto.request.UpdateMyProfileRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.dto.response.AttendanceResponse;
import com.hrms.dto.response.AttendanceSummaryResponse;
import com.hrms.dto.response.EmployeeAttendanceDashboardResponse;
import com.hrms.dto.response.EmployeeResponse;
import com.hrms.entity.Employee;
import com.hrms.service.EmployeeSelfServiceService;
import com.hrms.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {
	
	private final EmployeeService employeeService;
	private final EmployeeSelfServiceService employeeSelfServiceService;

	public EmployeeController(
	        EmployeeService employeeService,
	        EmployeeSelfServiceService employeeSelfServiceService) {

	    this.employeeService = employeeService;
	    this.employeeSelfServiceService = employeeSelfServiceService;
	}
	
	@GetMapping("/hello")
	public String hello() {

	    return "Welcome HRMS2";
	}
	
    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response =
                employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Employee created successfully.",
                                response
                        )
                );
    }
	
	
    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_VIEW')")
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> getEmployees(

            @RequestParam(defaultValue = "") String search,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "employeeCode") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Page<EmployeeResponse> response =
                employeeService.getEmployees(

                        search,

                        page,

                        size,

                        sortBy,

                        direction);

        return ResponseEntity.ok(

                ApiResponse.<Page<EmployeeResponse>>builder()

                        .success(true)

                        .message("Employees fetched successfully.")

                        .data(response)

                        .build());

    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_VIEW')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployee(
            @PathVariable Long id) {

        EmployeeResponse response =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .message("Employee fetched successfully.")
                        .data(response)
                        .build());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response =
                employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .message("Employee updated successfully.")
                        .data(response)
                        .build());
    }
	
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {

        employeeService.updateEmployeeStatus(id, active);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(active
                                ? "Employee activated successfully."
                                : "Employee deactivated successfully.")
                        .build());
    }
    
    @PutMapping("/me")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<ApiResponse<EmployeeResponse>>
    updateMyProfile(
            @RequestBody
            UpdateMyProfileRequest request) {

        return ResponseEntity.ok(

                ApiResponse.<EmployeeResponse>builder()

                        .success(true)

                        .message("Profile updated successfully.")

                        .data(employeeSelfServiceService.updateMyProfile(request))

                        .build());

    }
	
    @GetMapping("/profile")

    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")

    public ResponseEntity<ApiResponse<EmployeeResponse>>

    myProfile() {

        return ResponseEntity.ok(

                ApiResponse.<EmployeeResponse>builder()

                        .success(true)

                        .message("Profile fetched successfully.")

                        .data(employeeSelfServiceService.getMyProfile())

                        .build());

    }
    
    @PutMapping("/profile")

    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")

    public ResponseEntity<ApiResponse<EmployeeResponse>>

    updateProfile(

            @RequestBody

            @Valid

            UpdateMyProfileRequest request) {

        return ResponseEntity.ok(

                ApiResponse.<EmployeeResponse>builder()

                        .success(true)

                        .message("Profile updated successfully.")

                        .data(

                        		employeeService.updateMyProfile(request))

                        .build());

    }
    
    
    @GetMapping("/attendance")
    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getAttendance(

            @RequestParam LocalDate fromDate,

            @RequestParam LocalDate toDate,

            Pageable pageable) {

        return ResponseEntity.ok(

                ApiResponse.<Page<AttendanceResponse>>builder()

                        .success(true)

                        .message("Attendance history fetched successfully.")

                        .data(

                        		employeeSelfServiceService.getMyAttendance(fromDate, toDate, pageable)
                             )
                        .build());

    }
    
    @GetMapping("/attendance/monthly")
    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")
    public ResponseEntity<ApiResponse<AttendanceSummaryResponse>>
    getMonthlyAttendance(

            @RequestParam int month,

            @RequestParam int year) {

        return ResponseEntity.ok(

                ApiResponse.<AttendanceSummaryResponse>builder()

                        .success(true)

                        .message("Monthly attendance fetched successfully.")

                        .data(

                        		employeeSelfServiceService.getMonthlyAttendance(

                                        month,

                                        year))

                        .build());

    }
    
    @GetMapping("/attendance/dashboard")
    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")
    public ResponseEntity<ApiResponse<EmployeeAttendanceDashboardResponse>>
    dashboard(

            @RequestParam int month,

            @RequestParam int year) {

        return ResponseEntity.ok(

                ApiResponse.<EmployeeAttendanceDashboardResponse>builder()

                        .success(true)

                        .message("Attendance dashboard fetched successfully.")

                        .data(

                        		employeeSelfServiceService.getAttendanceDashboard(

                                        month,

                                        year))

                        .build());

    }
	
}

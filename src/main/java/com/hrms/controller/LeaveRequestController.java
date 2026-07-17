package com.hrms.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.request.LeaveApprovalRequest;
import com.hrms.dto.request.LeaveRequestRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.dto.response.LeaveDashboardResponse;
import com.hrms.dto.response.LeaveRequestResponse;
import com.hrms.enums.LeaveStatus;
import com.hrms.service.LeaveRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    @PreAuthorize("hasAuthority('LEAVE_APPLY')")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> applyLeave(
            @Valid @RequestBody LeaveRequestRequest request) {

        LeaveRequestResponse response =
                leaveRequestService.applyLeave(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<LeaveRequestResponse>builder()
                        .success(true)
                        .message("Leave request submitted successfully.")
                        .data(response)
                        .build());
    }
    
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> approve(
            @PathVariable Long id,
            @Valid @RequestBody LeaveApprovalRequest request) {

        return ResponseEntity.ok(

                ApiResponse.<LeaveRequestResponse>builder()

                        .success(true)

                        .message("Leave approved successfully.")

                        .data(
                                leaveRequestService
                                        .approveLeave(id, request))

                        .build());

    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> reject(
            @PathVariable Long id,
            @Valid @RequestBody LeaveApprovalRequest request) {

        return ResponseEntity.ok(

                ApiResponse.<LeaveRequestResponse>builder()

                        .success(true)

                        .message("Leave rejected successfully.")

                        .data(
                                leaveRequestService
                                        .rejectLeave(id, request))

                        .build());

    }
    
    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('LEAVE_VIEW')")
    public ResponseEntity<ApiResponse<Page<LeaveRequestResponse>>> history(

            @PathVariable Long employeeId,

            @RequestParam(required = false)
            LocalDate fromDate,

            @RequestParam(required = false)
            LocalDate toDate,

            @RequestParam(required = false)
            LeaveStatus status,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "fromDate")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction) {

        return ResponseEntity.ok(

                ApiResponse.<Page<LeaveRequestResponse>>builder()

                        .success(true)

                        .message("Leave history fetched successfully.")

                        .data(
                                leaveRequestService
                                        .getEmployeeLeaveHistory(
                                                employeeId,
                                                fromDate,
                                                toDate,
                                                status,
                                                page,
                                                size,
                                                sortBy,
                                                direction))

                        .build());

    }
    
    
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ResponseEntity<ApiResponse<Page<LeaveRequestResponse>>> pending(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long leaveTypeId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                ApiResponse.<Page<LeaveRequestResponse>>builder()
                        .success(true)
                        .message("Pending leave requests fetched successfully.")
                        .data(
                                leaveRequestService.getPendingRequests(
                                        departmentId,
                                        leaveTypeId,
                                        fromDate,
                                        toDate,
                                        page,
                                        size,
                                        sortBy,
                                        direction))
                        .build());
    }
    
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('LEAVE_VIEW')")
    public ResponseEntity<ApiResponse<LeaveDashboardResponse>> dashboard() {

        return ResponseEntity.ok(
                ApiResponse.<LeaveDashboardResponse>builder()
                        .success(true)
                        .message("Leave dashboard fetched successfully.")
                        .data(
                                leaveRequestService.getDashboard())
                        .build());
    }
    
    
    
    
    
    
    
    
}

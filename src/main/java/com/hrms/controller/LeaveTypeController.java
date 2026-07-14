package com.hrms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.request.LeaveTypeRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.dto.response.LeaveTypeResponse;
import com.hrms.service.LeaveTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {
	
	private final LeaveTypeService leaveTypeService;

    @PostMapping
    @PreAuthorize("hasAuthority('LEAVE_TYPE_CREATE')")
    public ResponseEntity<ApiResponse<LeaveTypeResponse>> create(
            @Valid @RequestBody LeaveTypeRequest request) {

        LeaveTypeResponse response =
                leaveTypeService.createLeaveType(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<LeaveTypeResponse>builder()
                        .success(true)
                        .message("Leave type created successfully.")
                        .data(response)
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('LEAVE_TYPE_VIEW')")
    public ResponseEntity<ApiResponse<List<LeaveTypeResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<LeaveTypeResponse>>builder()
                        .success(true)
                        .message("Leave types fetched successfully.")
                        .data(leaveTypeService.getAllLeaveTypes())
                        .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('LEAVE_TYPE_VIEW')")
    public ResponseEntity<ApiResponse<LeaveTypeResponse>> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<LeaveTypeResponse>builder()
                        .success(true)
                        .message("Leave type fetched successfully.")
                        .data(leaveTypeService.getLeaveTypeById(id))
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LEAVE_TYPE_UPDATE')")
    public ResponseEntity<ApiResponse<LeaveTypeResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody LeaveTypeRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<LeaveTypeResponse>builder()
                        .success(true)
                        .message("Leave type updated successfully.")
                        .data(leaveTypeService.updateLeaveType(id, request))
                        .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LEAVE_TYPE_DELETE')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id) {

        leaveTypeService.deleteLeaveType(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Leave type deleted successfully.")
                        .build());
    }

}

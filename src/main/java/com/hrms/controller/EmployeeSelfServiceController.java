package com.hrms.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.request.LeaveRequestRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.dto.response.LeaveDashboardResponse;
import com.hrms.dto.response.LeaveRequestResponse;
import com.hrms.dto.response.MyLeaveBalanceResponse;
import com.hrms.service.EmployeeSelfServiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ess")
@RequiredArgsConstructor
public class EmployeeSelfServiceController {


    private final EmployeeSelfServiceService service;


    @PostMapping("/leave/apply")
    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> apply(
            @RequestBody
            @Valid
            LeaveRequestRequest request) {


        return ResponseEntity.ok(

                ApiResponse.<LeaveRequestResponse>builder()

                        .success(true)

                        .message("Leave applied successfully.")

                        .data(
                                service.applyMyLeave(request)
                        )

                        .build()
        );
    }
    
    // ==========================
    // Leave Balance
    // ==========================

    @GetMapping("/leave/balances")
    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")
    public ResponseEntity<ApiResponse<List<MyLeaveBalanceResponse>>> getLeaveBalances() {


        return ResponseEntity.ok(

                ApiResponse.<List<MyLeaveBalanceResponse>>builder()

                        .success(true)

                        .message("Leave balances fetched successfully.")

                        .data(
                                service.getMyLeaveBalances()
                        )

                        .build()
        );
    }



    // ==========================
    // Leave History
    // ==========================

    @GetMapping("/leave/history")
    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")
    public ResponseEntity<ApiResponse<Page<LeaveRequestResponse>>> getLeaveHistory(
            Pageable pageable) {


        return ResponseEntity.ok(

                ApiResponse.<Page<LeaveRequestResponse>>builder()

                        .success(true)

                        .message("Leave history fetched successfully.")

                        .data(
                                service.getMyLeaveHistory(pageable)
                        )

                        .build()
        );
    }



    // ==========================
    // Apply Leave
    // ==========================




    // ==========================
    // Cancel Leave
    // ==========================

    @PutMapping("/leave/{id}/cancel")
    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> cancelLeave(
            @PathVariable Long id) {


        return ResponseEntity.ok(

                ApiResponse.<LeaveRequestResponse>builder()

                        .success(true)

                        .message("Leave cancelled successfully.")

                        .data(
                                service.cancelMyLeave(id)
                        )

                        .build()
        );
    }



    // ==========================
    // Leave Dashboard
    // ==========================

    @GetMapping("/leave/dashboard")
    @PreAuthorize("hasAuthority('EMPLOYEE_SELF_SERVICE')")
    public ResponseEntity<ApiResponse<LeaveDashboardResponse>> getLeaveDashboard() {


        return ResponseEntity.ok(

                ApiResponse.<LeaveDashboardResponse>builder()

                        .success(true)

                        .message("Leave dashboard fetched successfully.")

                        .data(
                                service.getLeaveDashboard()
                        )

                        .build()
        );
    }

}

package com.hrms.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.request.AttendanceRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.dto.response.AttendanceResponse;
import com.hrms.service.AttendanceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    @PreAuthorize("hasAuthority('ATTENDANCE_MARK')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(
            @Valid @RequestBody AttendanceRequest request) {

        AttendanceResponse response =
                attendanceService.checkIn(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<AttendanceResponse>builder()
                                .success(true)
                                .message("Check-in successful.")
                                .data(response)
                                .build());

    }
    
    
    @PostMapping("/check-out/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_MARK')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkOut(
            @PathVariable Long employeeId) {

        AttendanceResponse response =
                attendanceService.checkOut(employeeId);

        return ResponseEntity.ok(

                ApiResponse.<AttendanceResponse>builder()

                        .success(true)

                        .message("Check-out successful.")

                        .data(response)

                        .build());

    }
    
    @GetMapping("/date/{date}")
    @PreAuthorize("hasAuthority('ATTENDANCE_VIEW')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>>
    getAttendanceByDate(
            @PathVariable LocalDate date) {

        List<AttendanceResponse> response =
                attendanceService.getAttendanceByDate(date);

        return ResponseEntity.ok(

                ApiResponse.<List<AttendanceResponse>>builder()

                        .success(true)

                        .message("Attendance fetched successfully.")

                        .data(response)

                        .build());

    }
    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_VIEW')")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>>
    getEmployeeAttendance(

            @PathVariable Long employeeId,

            @RequestParam(required = false)
            LocalDate fromDate,

            @RequestParam(required = false)
            LocalDate toDate,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "attendanceDate")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction) {

        Page<AttendanceResponse> response =

                attendanceService.getEmployeeAttendance(

                        employeeId,

                        fromDate,

                        toDate,

                        page,

                        size,

                        sortBy,

                        direction);

        return ResponseEntity.ok(

                ApiResponse.<Page<AttendanceResponse>>builder()

                        .success(true)

                        .message("Attendance history fetched successfully.")

                        .data(response)

                        .build());

    }
    
    

}

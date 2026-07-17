package com.hrms.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.dto.request.LeaveApprovalRequest;
import com.hrms.dto.request.LeaveRequestRequest;
import com.hrms.dto.response.LeaveDashboardResponse;
import com.hrms.dto.response.LeaveRequestResponse;
import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;
import com.hrms.entity.LeaveBalance;
import com.hrms.entity.LeaveRequest;
import com.hrms.entity.LeaveType;
import com.hrms.entity.User;
import com.hrms.enums.AttendanceStatus;
import com.hrms.enums.LeaveStatus;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveBalanceRepository;
import com.hrms.repository.LeaveRequestRepository;
import com.hrms.repository.LeaveTypeRepository;
import com.hrms.repository.UserRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.LeaveRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveRequestServiceImpl implements LeaveRequestService{

    private final LeaveRequestRepository leaveRequestRepository;

    private final EmployeeRepository employeeRepository;

    private final LeaveTypeRepository leaveTypeRepository;

    private final LeaveBalanceRepository leaveBalanceRepository;

    private final CurrentUser currentUser;
    
    private final UserRepository userRepository;

    private final AttendanceRepository attendanceRepository;
	
    
    @Override
    public LeaveRequestResponse applyLeave(
            LeaveRequestRequest request) {

        Long companyId = currentUser.getCompanyId();

        Employee employee =
                validateEmployee(
                        request.getEmployeeId(),
                        companyId);

        LeaveType leaveType =
                validateLeaveType(
                        request.getLeaveTypeId(),
                        companyId);

        validateDates(
                request.getFromDate(),
                request.getToDate());

        Double totalDays =
                calculateLeaveDays(
                        request.getFromDate(),
                        request.getToDate());

        validateOverlappingLeave(

                employee.getId(),

                companyId,

                request.getFromDate(),

                request.getToDate());

        validateLeaveBalance(

                employee.getId(),

                leaveType.getId(),

                companyId,

                totalDays);

        LeaveRequest leaveRequest =
                new LeaveRequest();

        leaveRequest.setEmployee(employee);

        leaveRequest.setCompany(employee.getCompany());

        leaveRequest.setLeaveType(leaveType);

        leaveRequest.setFromDate(request.getFromDate());

        leaveRequest.setToDate(request.getToDate());

        leaveRequest.setTotalDays(totalDays);

        leaveRequest.setReason(request.getReason());

        leaveRequest.setStatus(LeaveStatus.PENDING);
        
        leaveRequest.setAppliedAt(LocalDateTime.now());

        leaveRequest = leaveRequestRepository.save(
                leaveRequest);

        return mapToResponse(leaveRequest);

    }
    
    @Override
    public LeaveRequestResponse approveLeave(
            Long leaveRequestId,
            LeaveApprovalRequest request) {

        Long companyId = currentUser.getCompanyId();

        LeaveRequest leaveRequest =
                leaveRequestRepository
                        .findByIdAndCompanyId(
                                leaveRequestId,
                                companyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Leave request not found."));

        validatePendingStatus(leaveRequest);

        LeaveBalance balance =
                getLeaveBalance(leaveRequest);

        balance.setUsedLeaves(
                balance.getUsedLeaves()
                        + leaveRequest.getTotalDays());

        balance.setRemainingLeaves(
                balance.getRemainingLeaves()
                        - leaveRequest.getTotalDays());

        leaveBalanceRepository.save(balance);

        User approver =
                userRepository.findById(
                        currentUser.getUserId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Approver not found."));

        leaveRequest.setStatus(
                LeaveStatus.APPROVED);

        leaveRequest.setApprovedAt(
                LocalDateTime.now());

        leaveRequest.setApproverRemarks(
                request.getRemarks());

        leaveRequest.setApprovedBy(
                approver);

        leaveRequestRepository.save(
                leaveRequest);

        markAttendanceAsLeave(
                leaveRequest);

        return mapToResponse(
                leaveRequest);

    }
    
    @Override
    public LeaveRequestResponse rejectLeave(
            Long leaveRequestId,
            LeaveApprovalRequest request) {

        Long companyId =
                currentUser.getCompanyId();

        LeaveRequest leaveRequest =
                leaveRequestRepository
                        .findByIdAndCompanyId(
                                leaveRequestId,
                                companyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Leave request not found."));

        validatePendingStatus(
                leaveRequest);

        User approver =
                userRepository
                        .findById(
                                currentUser.getUserId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Approver not found."));

        leaveRequest.setStatus(
                LeaveStatus.REJECTED);

        leaveRequest.setApprovedAt(
                LocalDateTime.now());

        leaveRequest.setApprovedBy(
                approver);

        leaveRequest.setApproverRemarks(
                request.getRemarks());

        leaveRequestRepository.save(
                leaveRequest);

        return mapToResponse(
                leaveRequest);

    }
    
    private void validatePendingStatus(
            LeaveRequest request) {

        if (request.getStatus()
                != LeaveStatus.PENDING) {

            throw new IllegalStateException(
                    "Only pending leave requests can be processed.");

        }

    }
    
    private LeaveBalance getLeaveBalance(
            LeaveRequest request) {

        return leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndCompanyIdAndYear(

                        request.getEmployee().getId(),

                        request.getLeaveType().getId(),

                        request.getCompany().getId(),

                        Year.now().getValue())

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Leave balance not found."));

    }
    
    private void markAttendanceAsLeave(
            LeaveRequest request) {

        LocalDate current =
                request.getFromDate();

        while (!current.isAfter(
                request.getToDate())) {

            Attendance attendance =
                    new Attendance();

            attendance.setEmployee(
                    request.getEmployee());

            attendance.setCompany(
                    request.getCompany());

            attendance.setAttendanceDate(
                    current);

            attendance.setStatus(
                    AttendanceStatus.LEAVE);

            attendanceRepository.save(
                    attendance);

            current = current.plusDays(1);

        }

    }
    
    
    
    private Employee validateEmployee(
            Long employeeId,
            Long companyId) {

        return employeeRepository
                .findByIdAndCompanyId(
                        employeeId,
                        companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found."));

    }
    
    private LeaveType validateLeaveType(
            Long leaveTypeId,
            Long companyId) {

        LeaveType leaveType =
                leaveTypeRepository
                        .findByIdAndCompanyId(
                                leaveTypeId,
                                companyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Leave type not found."));

        if (!Boolean.TRUE.equals(leaveType.getActive())) {

            throw new IllegalStateException(
                    "Leave type is inactive.");

        }

        return leaveType;

    }
    
    private void validateDates(
            LocalDate fromDate,
            LocalDate toDate) {

        if (fromDate.isAfter(toDate)) {

            throw new IllegalArgumentException(
                    "From date cannot be after To date.");

        }

        if (fromDate.isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Cannot apply leave for past dates.");

        }

    }
    
    private Double calculateLeaveDays(
            LocalDate fromDate,
            LocalDate toDate) {

        return (double)
                ChronoUnit.DAYS
                        .between(fromDate, toDate) + 1;

    }
    
    
    private void validateOverlappingLeave(

            Long employeeId,

            Long companyId,

            LocalDate fromDate,

            LocalDate toDate) {

        boolean exists =
                leaveRequestRepository
                        .existsOverlappingLeave(

                                employeeId,

                                companyId,

                                fromDate,

                                toDate);

        if (exists) {

            throw new DuplicateResourceException(
                    "Overlapping leave request exists.");

        }

    }
    
    
    private void validateLeaveBalance(

            Long employeeId,

            Long leaveTypeId,

            Long companyId,

            Double requestedDays) {

        LeaveBalance balance =
                leaveBalanceRepository
                        .findByEmployeeIdAndLeaveTypeIdAndCompanyIdAndYear(

                                employeeId,

                                leaveTypeId,

                                companyId,

                                Year.now().getValue())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Leave balance not found."));

        if (balance.getRemainingLeaves() < requestedDays) {

            throw new IllegalArgumentException(
                    "Insufficient leave balance.");

        }

    }
    
    private LeaveRequestResponse mapToResponse(
            LeaveRequest request) {

        Employee employee = request.getEmployee();

        return LeaveRequestResponse.builder()
                .id(request.getId())
                .employeeId(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .leaveType(request.getLeaveType().getLeaveName())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .totalDays(request.getTotalDays())
                .reason(request.getReason())
                .status(request.getStatus())
                .build();

    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestResponse> getEmployeeLeaveHistory(

            Long employeeId,

            LocalDate fromDate,

            LocalDate toDate,

            LeaveStatus status,

            int page,

            int size,

            String sortBy,

            String direction) {

        Long companyId = currentUser.getCompanyId();

        validateEmployee(employeeId, companyId);

        Pageable pageable = PageRequest.of(
                page,
                size,
                direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending());

        Page<LeaveRequest> leaveRequests;

        if (status != null) {

            leaveRequests =
                    leaveRequestRepository
                            .findByEmployeeIdAndCompanyIdAndStatus(
                                    employeeId,
                                    companyId,
                                    status,
                                    pageable);

        } else if (fromDate != null && toDate != null) {

            leaveRequests =
                    leaveRequestRepository
                            .findByEmployeeAndDateRange(
                                    employeeId,
                                    companyId,
                                    fromDate,
                                    toDate,
                                    pageable);

        } else {

            leaveRequests =
                    leaveRequestRepository
                            .findByEmployeeIdAndCompanyId(
                                    employeeId,
                                    companyId,
                                    pageable);

        }

        return leaveRequests.map(this::mapToResponse);

    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestResponse> getPendingRequests(

            Long departmentId,

            Long leaveTypeId,

            LocalDate fromDate,

            LocalDate toDate,

            int page,

            int size,

            String sortBy,

            String direction) {

        Long companyId = currentUser.getCompanyId();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        direction.equalsIgnoreCase("desc")
                                ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending());

        Page<LeaveRequest> requests;

        if (departmentId != null) {

            requests =
                    leaveRequestRepository
                            .findPendingByDepartment(
                                    companyId,
                                    LeaveStatus.PENDING,
                                    departmentId,
                                    pageable);

        } else if (leaveTypeId != null) {

            requests =
                    leaveRequestRepository
                            .findByCompanyIdAndStatusAndLeaveTypeId(
                                    companyId,
                                    LeaveStatus.PENDING,
                                    leaveTypeId,
                                    pageable);

        } else if (fromDate != null && toDate != null) {

            requests =
                    leaveRequestRepository
                            .findPendingBetweenDates(
                                    companyId,
                                    LeaveStatus.PENDING,
                                    fromDate,
                                    toDate,
                                    pageable);

        } else {

            requests =
                    leaveRequestRepository
                            .findByCompanyIdAndStatus(
                                    companyId,
                                    LeaveStatus.PENDING,
                                    pageable);

        }

        return requests.map(this::mapToResponse);

    }
    
    
    @Override
    @Transactional(readOnly = true)
    public LeaveDashboardResponse getDashboard() {

        Long companyId = currentUser.getCompanyId();

        LocalDate today = LocalDate.now();

        LocalDateTime startToday =
                today.atStartOfDay();

        LocalDateTime endToday =
                today.atTime(23,59,59);

        LocalDate firstDay =
                today.withDayOfMonth(1);

        LeaveDashboardResponse response =
                LeaveDashboardResponse.builder()

                        .totalPending(
                                leaveRequestRepository
                                        .countByCompanyIdAndStatus(
                                                companyId,
                                                LeaveStatus.PENDING))

                        .pendingToday(
                                leaveRequestRepository
                                        .countByCompanyIdAndStatusAndAppliedAtBetween(
                                                companyId,
                                                LeaveStatus.PENDING,
                                                startToday,
                                                endToday))

                        .approvedThisMonth(
                                leaveRequestRepository
                                        .countByCompanyIdAndStatusAndApprovedAtBetween(
                                                companyId,
                                                LeaveStatus.APPROVED,
                                                firstDay.atStartOfDay(),
                                                LocalDateTime.now()))

                        .employeesCurrentlyOnLeave(
                                leaveRequestRepository
                                        .countEmployeesCurrentlyOnLeave(
                                                companyId))

                        .build();

        return response;

    }
    
    
    
    
}

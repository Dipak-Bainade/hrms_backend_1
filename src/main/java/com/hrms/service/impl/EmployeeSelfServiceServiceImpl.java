package com.hrms.service.impl;

import java.math.BigDecimal;

import java.math.RoundingMode;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.dto.request.LeaveRequestRequest;
import com.hrms.dto.request.UpdateMyProfileRequest;
import com.hrms.dto.response.AttendanceResponse;
import com.hrms.dto.response.AttendanceSummaryResponse;
import com.hrms.dto.response.EmployeeAttendanceDashboardResponse;
import com.hrms.dto.response.EmployeeResponse;
import com.hrms.dto.response.LeaveDashboardResponse;
import com.hrms.dto.response.LeaveRequestResponse;
import com.hrms.dto.response.MyLeaveBalanceResponse;
import com.hrms.dto.response.PayslipDashboardResponse;
import com.hrms.dto.response.PayslipDetailResponse;
import com.hrms.dto.response.PayslipSummaryResponse;
import com.hrms.entity.Attendance;
import com.hrms.entity.Employee;
import com.hrms.entity.LeaveRequest;
import com.hrms.entity.Payroll;
import com.hrms.enums.AttendanceStatus;
import com.hrms.enums.LeaveStatus;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveBalanceRepository;
import com.hrms.repository.LeaveRequestRepository;
import com.hrms.repository.PayrollRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.EmployeeSelfServiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeSelfServiceServiceImpl implements EmployeeSelfServiceService{

	private final EmployeeRepository employeeRepository;

	private final CurrentUser currentUser;
	
	private final AttendanceRepository attendanceRepository;
	
	private final LeaveBalanceRepository leaveBalanceRepository;
	
	private final LeaveRequestRepository leaveRequestRepository;
	
	private final PayrollRepository payrollRepository;
	
	
	@Override
	@Transactional(readOnly = true)
	public EmployeeResponse getMyProfile() {

	    Employee employee = employeeRepository

	            .findByUserIdAndCompanyId(

	                    currentUser.getUserId(),

	                    currentUser.getCompanyId())

	            .orElseThrow(() ->

	                    new ResourceNotFoundException(
	                            "Employee not found"));

	    return mapToResponse(employee);

	}
	
	private EmployeeResponse mapToResponse(Employee employee) {

	    return EmployeeResponse.builder()
	            .id(employee.getId())
	            .employeeCode(employee.getEmployeeCode())
	            .firstName(employee.getFirstName())
	            .lastName(employee.getLastName())
	            .email(employee.getEmail())
	            .mobile(employee.getMobile())
	            .address(employee.getAddress())
	            .city(employee.getCity())
	            .state(employee.getState())
	            .pinCode(employee.getPinCode())
	            .build();
	}

	@Override
	public EmployeeResponse updateMyProfile(UpdateMyProfileRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	private Employee getLoggedInEmployee() {

	    return employeeRepository
	            .findByUserIdAndCompanyId(
	                    currentUser.getUserId(),
	                    currentUser.getCompanyId())
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Employee not found"));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AttendanceResponse> getMyAttendance(

	        LocalDate fromDate,

	        LocalDate toDate,

	        Pageable pageable) {

	    Employee employee = getLoggedInEmployee();

	    return attendanceRepository

	            .findByEmployeeIdAndAttendanceDateBetweenOrderByAttendanceDateDesc(

	                    employee.getId(),

	                    fromDate,

	                    toDate,

	                    pageable)

	            .map(this::mapAttendanceResponse);

	}

	@Override
	@Transactional(readOnly = true)
	public AttendanceSummaryResponse getMonthlyAttendance(

	        int month,

	        int year) {

	    Employee employee = getLoggedInEmployee();

	    LocalDate start =

	            LocalDate.of(year, month, 1);

	    LocalDate end =

	            start.withDayOfMonth(

	                    start.lengthOfMonth());

	    List<Attendance> attendanceList =

	            attendanceRepository

	                    .findByEmployeeIdAndAttendanceDateBetween(

	                            employee.getId(),

	                            start,

	                            end);

	    long present = attendanceList.stream()

	            .filter(a ->

	                    a.getStatus() == AttendanceStatus.PRESENT)

	            .count();

	    long absent = attendanceList.stream()

	            .filter(a ->

	                    a.getStatus() == AttendanceStatus.ABSENT)

	            .count();

	    long leave = attendanceList.stream()

	            .filter(a ->

	                    a.getStatus() == AttendanceStatus.LEAVE)

	            .count();

	    long holiday = attendanceList.stream()

	            .filter(a ->

	                    a.getStatus() == AttendanceStatus.HOLIDAY)

	            .count();

	  
	    BigDecimal totalHours = attendanceList.stream()

	            .map(Attendance::getWorkingHours)

	            .filter(Objects::nonNull)

	            .map(BigDecimal::valueOf)

	            .reduce(BigDecimal.ZERO, BigDecimal::add);

	    BigDecimal averageHours =

	            present == 0

	                    ? BigDecimal.ZERO

	                    : totalHours.divide(
	                            BigDecimal.valueOf(present),
	                            2,
	                            RoundingMode.HALF_UP);
	   
	    

	    return AttendanceSummaryResponse.builder()

	            .presentDays(present)

	            .absentDays(absent)

	            .leaveDays(leave)

	            .holidayDays(holiday)

	            .totalWorkingHours(totalHours)

	            .averageWorkingHours(averageHours)

	            .build();

	}

	
	@Override
	@Transactional(readOnly = true)
	public EmployeeAttendanceDashboardResponse getAttendanceDashboard(

	        int month,

	        int year) {

	    Employee employee =

	            getLoggedInEmployee();

	    return EmployeeAttendanceDashboardResponse.builder()

	            .summary(

	                    getMonthlyAttendance(

	                            month,

	                            year))

	            .recentAttendance(

	                    attendanceRepository

	                            .findTop10ByEmployeeIdOrderByAttendanceDateDesc(

	                                    employee.getId())

	                            .stream()

	                            .map(this::mapAttendanceResponse)

	                            .toList())

	            .build();

	}
	
	
	private AttendanceResponse mapAttendanceResponse(
	        Attendance attendance) {

	    Employee employee = attendance.getEmployee();

	    return new AttendanceResponse(
	            attendance.getId(),
	            employee.getEmployeeCode(),
	            employee.getFirstName() + " " + employee.getLastName(),
	            attendance.getAttendanceDate(),
	            attendance.getCheckInTime(),
	            attendance.getCheckOutTime(),
	            attendance.getWorkingHours(),
	            attendance.getStatus(),
	            attendance.getRemarks()
	    );
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<MyLeaveBalanceResponse> getMyLeaveBalances() {

	    Employee employee = getLoggedInEmployee();

	    List<com.hrms.entity.LeaveBalance> balances =
	            leaveBalanceRepository
	                    .findByEmployeeIdAndActiveTrue(employee.getId());


	    return balances.stream()
	            .map(balance -> {

	                MyLeaveBalanceResponse response =
	                        MyLeaveBalanceResponse.builder()
	                                .leaveType(
	                                    balance.getLeaveType()
	                                            .getLeaveTypeName())
	                                .allocatedDays(
	                                    balance.getAllocatedLeaves().intValue())
	                                .usedDays(
	                                    balance.getUsedLeaves().intValue())
	                                .remainingDays(
	                                    balance.getRemainingLeaves().intValue())
	                                .build();

	                return response;
	            })
	            .toList();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Page<LeaveRequestResponse> getMyLeaveHistory(
	        Pageable pageable) {

	    Employee employee = getLoggedInEmployee();

	    return leaveRequestRepository
	            .findByEmployee_IdOrderByAppliedAtDesc(
	                    employee.getId(),
	                    pageable)
	            .map(this::mapLeaveResponse);
	}
	

	private LeaveRequestResponse mapLeaveResponse(
	        com.hrms.entity.LeaveRequest leaveRequest) {

	    return LeaveRequestResponse.builder()

	            .leaveType(
	                    leaveRequest.getLeaveType()
	                            .getLeaveTypeName())

	            .fromDate(
	                    leaveRequest.getFromDate())

	            .toDate(
	                    leaveRequest.getToDate())

	            .totalDays(
	                    leaveRequest.getTotalDays())

	            .status(
	                    leaveRequest.getStatus())

	            .reason(
	                    leaveRequest.getReason())

	            .build();
	}
	
	@Override
	@Transactional
	public LeaveRequestResponse applyMyLeave(
	        LeaveRequestRequest request) {

	    Employee employee = getLoggedInEmployee();

	    // Leave apply logic will be added here

	    return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public LeaveDashboardResponse getLeaveDashboard() {

	    Employee employee = getLoggedInEmployee();


	    return LeaveDashboardResponse.builder()

	            .pending(
	                    leaveRequestRepository.countPending(
	                            employee.getId()))

	            .approved(
	                    leaveRequestRepository.countApproved(
	                            employee.getId()))

	            .rejected(
	                    leaveRequestRepository.countRejected(
	                            employee.getId()))

	            .cancelled(
	                    leaveRequestRepository.countCancelled(
	                            employee.getId()))

	            .upcomingLeaves(
	                    leaveRequestRepository.countUpcoming(
	                            employee.getId(),
	                            LocalDate.now()))

	            .balances(
	                    getMyLeaveBalances())

	            .build();
	}
	
	
	@Override
	@Transactional
	public LeaveRequestResponse cancelMyLeave(Long id) {

	    Employee employee = getLoggedInEmployee();


	    LeaveRequest leaveRequest =
	            leaveRequestRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Leave request not found"));


	    if(!leaveRequest.getEmployee()
	            .getId()
	            .equals(employee.getId())) {

	        throw new RuntimeException(
	                "You cannot cancel this leave");
	    }


	    leaveRequest.setStatus(
	            LeaveStatus.CANCELLED);


	    LeaveRequest saved =
	            leaveRequestRepository.save(
	                    leaveRequest);


	    return mapLeaveResponse(saved);
	}

	
	
	@Override
	@Transactional(readOnly = true)
	public Page<PayslipSummaryResponse> getMyPayslips(

	        Integer month,

	        Integer year,

	        Pageable pageable) {

	    Employee employee = getLoggedInEmployee();

	    Page<Payroll> payrolls;

	    if (month != null && year != null) {

	        payrolls = payrollRepository

	                .findByEmployeeIdAndMonthAndYear(

	                        employee.getId(),

	                        month,

	                        year,

	                        pageable);

	    } else {

	        payrolls = payrollRepository

	                .findByEmployee_IdOrderByPayYearDescPayMonthDesc(

	                        employee.getId(),

	                        pageable);

	    }

	    return payrolls.map(this::mapPayslipSummary);

	}
	
	private PayslipSummaryResponse mapPayslipSummary(Payroll payroll) {

	    return PayslipSummaryResponse.builder()
	            .payrollId(payroll.getId())
	            .month(payroll.getPayMonth())
	            .year(payroll.getPayYear())
	            .grossSalary(BigDecimal.valueOf(payroll.getGrossSalary()))
	            .deductions(BigDecimal.valueOf(payroll.getTotalDeduction()))
	            .netSalary(BigDecimal.valueOf(payroll.getNetSalary()))
	            .processedDate(
	                    payroll.getProcessedAt() != null
	                            ? payroll.getProcessedAt().toLocalDate()
	                            : null)
	            .build();
	}
	

	@Override
	@Transactional(readOnly = true)
	public PayslipDetailResponse getMyPayslip(

	        Long payrollId) {

	    Employee employee = getLoggedInEmployee();

	    Payroll payroll = payrollRepository

	            .findById(payrollId)

	            .orElseThrow(() ->

	                    new ResourceNotFoundException(

	                            "Payroll not found"));

	    if (!payroll.getEmployee()

	            .getId()

	            .equals(employee.getId())) {

	        throw new AccessDeniedException(

	                "Unauthorized access.");

	    }

	    return mapPayslipDetail(payroll);

	}
	
	private PayslipDetailResponse mapPayslipDetail(Payroll payroll) {

	    Employee employee = payroll.getEmployee();

	    return PayslipDetailResponse.builder()
	            .payrollId(payroll.getId())
	            .employeeCode(employee.getEmployeeCode())
	            .employeeName(employee.getFirstName() + " " + employee.getLastName())
	            .department(
	                    employee.getDepartment() != null
	                            ? employee.getDepartment().getDepartmentName()
	                            : null)
	            .designation(
	                    employee.getDesignation() != null
	                            ? employee.getDesignation().getDesignationName()
	                            : null)
	            .month(payroll.getPayMonth())
	            .year(payroll.getPayYear())
	            .grossSalary(BigDecimal.valueOf(payroll.getGrossSalary()))
	            .allowances(BigDecimal.valueOf(payroll.getTotalAllowance()))
	            .deductions(BigDecimal.valueOf(payroll.getTotalDeduction()))
	            .netSalary(BigDecimal.valueOf(payroll.getNetSalary()))
	            .overtimeAmount(BigDecimal.valueOf(payroll.getOvertimeAmount()))
	            .lopDeduction(BigDecimal.valueOf(payroll.getLopDeduction()))
	            .build();
	}

	@Override
	public byte[] downloadMyPayslip(

	        Long payrollId) {

	    Employee employee = getLoggedInEmployee();

	    Payroll payroll = payrollRepository

	            .findById(payrollId)

	            .orElseThrow(() ->

	                    new ResourceNotFoundException(

	                            "Payroll not found"));

	    if (!payroll.getEmployee()

	            .getId()

	            .equals(employee.getId())) {

	        throw new AccessDeniedException(

	                "Unauthorized access.");

	    }

	    return payslipService.generatePayslip(

	            payroll.getId());

	}

	@Override
	@Transactional(readOnly = true)
	public PayslipDashboardResponse getPayrollDashboard() {

	    Employee employee = getLoggedInEmployee();

	    Payroll latest = payrollRepository

	            .findTopByEmployeeIdOrderByYearDescMonthDesc(

	                    employee.getId())

	            .orElse(null);

	    BigDecimal earnings = payrollRepository

	            .sumGrossSalaryByEmployeeAndYear(

	                    employee.getId(),

	                    Year.now().getValue());

	    BigDecimal deductions = payrollRepository

	            .sumTotalDeductionByEmployeeAndYear(

	                    employee.getId(),

	                    Year.now().getValue());

	    return PayslipDashboardResponse.builder()

	            .latestNetSalary(

	                    latest == null

	                            ? BigDecimal.ZERO

	                            : latest.getNetSalary())

	            .latestPayrollDate(

	                    latest == null

	                            ? null

	                            : latest.getProcessedDate())

	            .yearToDateEarnings(

	                    earnings == null

	                            ? BigDecimal.ZERO

	                            : earnings)

	            .yearToDateDeductions(

	                    deductions == null

	                            ? BigDecimal.ZERO

	                            : deductions)

	            .processedPayrolls(

	                    payrollRepository.countByEmployeeId(

	                            employee.getId()))

	            .build();

	}
	
	
	
	
	
	
	
	
}

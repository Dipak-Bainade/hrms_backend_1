package com.hrms.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hrms.dto.payroll.AttendanceSummary;
import com.hrms.dto.request.AttendanceRequest;
import com.hrms.dto.response.AttendanceResponse;
import com.hrms.dto.response.MonthlyAttendanceResponse;
import com.hrms.entity.Attendance;
import com.hrms.entity.Company;
import com.hrms.entity.Employee;
import com.hrms.enums.AttendanceStatus;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.CompanyRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.AttendanceService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{


    private final AttendanceRepository attendanceRepository;

    private final EmployeeRepository employeeRepository;

    private final CompanyRepository companyRepository;

    private final CurrentUser currentUser;

    @Override
    public AttendanceResponse checkIn(
            AttendanceRequest request) {

        Long companyId = currentUser.getCompanyId();

        Employee employee = validateEmployee(
                request.getEmployeeId(),
                companyId);

        validateDuplicateAttendance(
                employee.getId(),
                request.getAttendanceDate());

        Company company = companyRepository
                .findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found."));

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);

        attendance.setCompany(company);

        attendance.setAttendanceDate(
                request.getAttendanceDate());

        attendance.setCheckInTime(
                LocalDateTime.now());

        attendance.setStatus(
                AttendanceStatus.PRESENT);

        attendance.setRemarks(
                request.getRemarks());

        attendance.setActive(true);

        attendance = attendanceRepository.save(attendance);

        return mapToResponse(attendance);
    }
    

    
    
    
    private AttendanceResponse mapToResponse(
            Attendance attendance) {

        Employee employee = attendance.getEmployee();

        return new AttendanceResponse(

                attendance.getId(),

                employee.getEmployeeCode(),

                employee.getFirstName()
                        + " "
                        + employee.getLastName(),

                attendance.getAttendanceDate(),

                attendance.getCheckInTime(),

                attendance.getCheckOutTime(),

                attendance.getWorkingHours(),

                attendance.getStatus(),

                attendance.getRemarks()

        );

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

    
    private void validateDuplicateAttendance(
            Long employeeId,
            LocalDate date) {

        if (attendanceRepository
                .findByEmployeeIdAndAttendanceDate(
                        employeeId,
                        date)
                .isPresent()) {

            throw new DuplicateResourceException(
                    "Attendance already marked for today.");

        }

    }

    @Override
    public AttendanceResponse checkOut(Long employeeId) {

        Long companyId = currentUser.getCompanyId();

        validateEmployee(employeeId, companyId);

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndCompanyIdAndAttendanceDate(
                        employeeId,
                        companyId,
                        LocalDate.now())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No check-in found for today."));

        validateCheckOut(attendance);

        attendance.setCheckOutTime(LocalDateTime.now());

        attendance.setWorkingHours(
                calculateWorkingHours(
                        attendance.getCheckInTime(),
                        attendance.getCheckOutTime()));

        attendanceRepository.save(attendance);

        return mapToResponse(attendance);
    }
    
    private void validateCheckOut(
            Attendance attendance) {

        if (attendance.getCheckInTime() == null) {

            throw new IllegalStateException(
                    "Employee has not checked in.");

        }
        

        if (attendance.getCheckOutTime() != null) {

            throw new DuplicateResourceException(
                    "Employee has already checked out.");

        }

    }
    
    private Double calculateWorkingHours(
            LocalDateTime checkIn,
            LocalDateTime checkOut) {

        Duration duration =
                Duration.between(checkIn, checkOut);

        return duration.toMinutes() / 60.0;

    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByDate(
            LocalDate attendanceDate) {

        Long companyId = currentUser.getCompanyId();

        List<Attendance> attendanceList =
                attendanceRepository
                        .findByCompanyIdAndAttendanceDate(
                                companyId,
                                attendanceDate);

        return attendanceList.stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getEmployeeAttendance(

            Long employeeId,

            LocalDate fromDate,

            LocalDate toDate,

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

        Page<Attendance> attendance;

        if (fromDate == null || toDate == null) {

            attendance = attendanceRepository

                    .findByEmployeeIdAndCompanyId(

                            employeeId,

                            companyId,

                            pageable);

        } else {

            attendance = attendanceRepository

                    .findAttendanceBetweenDates(

                            employeeId,

                            companyId,

                            fromDate,

                            toDate,

                            pageable);

        }

        return attendance.map(this::mapToResponse);

    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonthlyAttendanceResponse> getMonthlyAttendance(

            Long employeeId,

            int month,

            int year) {

        Long companyId = currentUser.getCompanyId();

        List<Attendance> attendanceList =
                attendanceRepository.findMonthlyAttendance(

                        companyId,

                        employeeId,

                        month,

                        year);

        Map<Long, List<Attendance>> employeeAttendance =
                attendanceList.stream()

                        .collect(Collectors.groupingBy(
                                attendance ->
                                        attendance.getEmployee().getId()));

        return employeeAttendance.values()

                .stream()

                .map(this::buildMonthlyAttendanceResponse)

                .toList();

    }

    private MonthlyAttendanceResponse buildMonthlyAttendanceResponse(
            List<Attendance> attendanceList) {

        Employee employee = attendanceList.get(0).getEmployee();

        long present = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                .count();

        long absent = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT)
                .count();

        long leave = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.LEAVE)
                .count();

        long holiday = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.HOLIDAY)
                .count();

        long weekOff = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.WEEK_OFF)
                .count();

        long workFromHome = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.WORK_FROM_HOME)
                .count();

        double totalHours = attendanceList.stream()
                .filter(a -> a.getWorkingHours() != null)
                .mapToDouble(Attendance::getWorkingHours)
                .sum();

        double averageHours = present > 0
                ? totalHours / present
                : 0;

        return MonthlyAttendanceResponse.builder()

                .employeeId(employee.getId())

                .employeeCode(employee.getEmployeeCode())

                .employeeName(employee.getFirstName() + " " + employee.getLastName())

                .month(attendanceList.get(0).getAttendanceDate().getMonthValue())

                .year(attendanceList.get(0).getAttendanceDate().getYear())

                .totalPresentDays(present)

                .totalAbsentDays(absent)

                .totalLeaveDays(leave)

                .totalHolidayDays(holiday)

                .totalWeekOffDays(weekOff)

                .totalWorkFromHomeDays(workFromHome)

                .totalWorkingHours(totalHours)

                .averageWorkingHours(averageHours)

                .build();

    }




	@Override
	public List<AttendanceResponse> getEmployeeAttendance(Long employeeId) {
		// TODO Auto-generated method stub
		return null;
	}





	@Override
	public AttendanceSummary getAttendanceSummary(Long employeeId, Integer month, Integer year) {
		// TODO Auto-generated method stub
		return null;
	}
}

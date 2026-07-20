package com.hrms.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hrms.dto.payroll.AttendanceSummary;
import com.hrms.dto.payroll.PayrollCalculationResult;
import com.hrms.dto.payroll.PayslipResponse;
import com.hrms.dto.request.PayrollRequest;
import com.hrms.dto.response.PayrollDashboardResponse;
import com.hrms.dto.response.PayrollResponse;
import com.hrms.entity.Employee;
import com.hrms.entity.EmployeeSalary;
import com.hrms.entity.Payroll;
import com.hrms.entity.SalaryStructure;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.payroll.SalaryCalculator;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.EmployeeSalaryRepository;
import com.hrms.repository.PayrollRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.AttendanceService;
import com.hrms.service.PayrollService;


import lombok.RequiredArgsConstructor;
import java.io.ByteArrayOutputStream;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

@RequiredArgsConstructor
@Service
@Transactional
public class PayrollServiceImpl
        implements PayrollService {

    private final CurrentUser currentUser;

    private final EmployeeRepository employeeRepository;

    private final EmployeeSalaryRepository employeeSalaryRepository;

    private final PayrollRepository payrollRepository;

    private final AttendanceService attendanceService;

    private final SalaryCalculator salaryCalculator;

    
    @Override
    public PayrollResponse processPayroll(
            PayrollRequest request) {

        Long companyId =
                currentUser.getCompanyId();

        Employee employee =
                validateEmployee(
                        request.getEmployeeId(),
                        companyId);

        EmployeeSalary employeeSalary =
                getActiveSalary(
                        employee.getId(),
                        companyId);

        AttendanceSummary attendance =
                attendanceService
                        .getAttendanceSummary(
                                employee.getId(),
                                request.getMonth(),
                                request.getYear());

        SalaryStructure structure =
                employeeSalary.getSalaryStructure();

        PayrollCalculationResult result =
                salaryCalculator.calculate(
                        structure,
                        attendance);

        Payroll payroll =
                createPayroll(
                        request,
                        employee,
                        employeeSalary,
                        attendance,
                        result);

        payroll =
                payrollRepository.save(payroll);

        return mapToResponse(payroll);

    }
    
    private PayrollResponse mapToResponse(
            Payroll payroll) {

        Employee employee = payroll.getEmployee();

        return PayrollResponse.builder()

        		.payrollId(payroll.getId())

                .employeeId(employee.getId())

                .employeeCode(employee.getEmployeeCode())

                .employeeName(
                        employee.getFirstName()
                                + " "
                                + employee.getLastName())

                .month(payroll.getPayMonth())

                .year(payroll.getPayYear())

                .workingDays(payroll.getWorkingDays())

                .presentDays(payroll.getPresentDays())

                .leaveDays(payroll.getLeaveDays())

                .lopDays(payroll.getLopDays())

                .overtimeHours(payroll.getOvertimeHours())

                .grossSalary(payroll.getGrossSalary())

                .totalAllowance(payroll.getTotalAllowance())

                .totalDeduction(payroll.getTotalDeduction())

                .lopDeduction(payroll.getLopDeduction())

                .overtimeAmount(payroll.getOvertimeAmount())

                .netSalary(payroll.getNetSalary())

                .processed(payroll.getProcessed())

                .processedAt(payroll.getProcessedAt())

                .build();
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
                                "Employee not found"));

    }
    
    
    private EmployeeSalary getActiveSalary(

            Long employeeId,

            Long companyId) {

        return employeeSalaryRepository

                .findByEmployeeIdAndCompanyIdAndActiveTrue(
                        employeeId,
                        companyId)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee salary not assigned"));

    }
    
    
    
    private Payroll createPayroll(

            PayrollRequest request,

            Employee employee,

            EmployeeSalary employeeSalary,

            AttendanceSummary attendance,

            PayrollCalculationResult result) {

        Payroll payroll =
                new Payroll();

        payroll.setEmployee(employee);

        payroll.setCompany(employee.getCompany());

        payroll.setEmployeeSalary(employeeSalary);

        payroll.setPayMonth(request.getMonth());

        payroll.setPayYear(request.getYear());

        payroll.setWorkingDays(attendance.getWorkingDays());

        payroll.setPresentDays(attendance.getPresentDays());

        payroll.setLeaveDays(attendance.getApprovedLeaveDays());

        payroll.setLopDays(attendance.getLopDays());

        payroll.setOvertimeHours(attendance.getOvertimeHours());

        payroll.setGrossSalary(result.getGrossSalary());

        payroll.setTotalAllowance(result.getAllowance());

        payroll.setTotalDeduction(result.getDeduction());

        payroll.setLopDeduction(result.getLopDeduction());

        payroll.setOvertimeAmount(result.getOvertimeAmount());

        payroll.setNetSalary(result.getNetSalary());

        payroll.setProcessed(true);

        payroll.setProcessedAt(LocalDateTime.now());

        return payroll;

    }

	@Override
	public PayrollResponse reprocessPayroll(PayrollRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayrollResponse getPayroll(Long employeeId, Integer month, Integer year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PayrollResponse> getPayrollHistory(Long employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public PayslipResponse generatePayslip(Long payrollId) {
		Long companyId =
	            currentUser.getCompanyId();

	    Payroll payroll =
	            payrollRepository

	                    .findByIdAndCompanyId(
	                            payrollId,
	                            companyId)

	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Payroll not found"));

	    Employee employee =
	            payroll.getEmployee();

	    SalaryStructure salary =
	            payroll.getEmployeeSalary()
	                    .getSalaryStructure();

	    return PayslipResponse.builder()

	            .companyName(
	                    employee.getCompany().getCompanyName())

	            .companyAddress(
	                    employee.getCompany().getAddress())

	            .companyLogo(
	                    employee.getCompany().getLogo())

	            .employeeCode(
	                    employee.getEmployeeCode())

	            .employeeName(
	                    employee.getFirstName()
	                            + " "
	                            + employee.getLastName())

	            .designation(
	                    employee.getDesignation()
	                            .getDesignationName())

	            .department(
	                    employee.getDepartment()
	                            .getDepartmentName())

	            .month(payroll.getPayMonth())

	            .year(payroll.getPayYear())

	            .workingDays(
	                    payroll.getWorkingDays())

	            .presentDays(
	                    payroll.getPresentDays())

	            .leaveDays(
	                    payroll.getLeaveDays())

	            .lopDays(
	                    payroll.getLopDays())

	            .basicSalary(
	                    salary.getBasicSalary())

	            .hra(
	                    salary.getHra())

	            .da(
	                    salary.getDa())

	            .specialAllowance(
	                    salary.getSpecialAllowance())

	            .otherAllowance(
	                    salary.getOtherAllowance())

	            .pf(
	                    salary.getPf())

	            .professionalTax(
	                    salary.getProfessionalTax())

	            .otherDeduction(
	                    salary.getOtherDeduction())

	            .overtimeAmount(
	                    payroll.getOvertimeAmount())

	            .lopDeduction(
	                    payroll.getLopDeduction())

	            .grossSalary(
	                    payroll.getGrossSalary())

	            .totalDeduction(
	                    payroll.getTotalDeduction())

	            .netSalary(
	                    payroll.getNetSalary())

	            .build();
	}

	
	@Override
	@Transactional(readOnly = true)
	public byte[] downloadPayslip(Long payrollId) {

	    PayslipResponse payslip = generatePayslip(payrollId);

	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

	        PdfWriter writer = new PdfWriter(outputStream);

	        PdfDocument pdfDocument = new PdfDocument(writer);

	        Document document = new Document(pdfDocument);
	        
	        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
	        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

	        // Company
	        document.add(new Paragraph(payslip.getCompanyName())
	                .setFont(boldFont)
	                .setFontSize(18));

	        document.add(new Paragraph(payslip.getCompanyAddress()));

	        document.add(new Paragraph(" "));

	        document.add(new Paragraph("PAYSLIP")
	        		.setFont(boldFont)
	                .setFontSize(16));

	        document.add(new Paragraph(
	                "Month : " + payslip.getMonth() + "/" + payslip.getYear()));

	        document.add(new Paragraph(" "));

	        // Employee Details
	        Table employeeTable = new Table(2);

	        employeeTable.addCell("Employee Code");
	        employeeTable.addCell(payslip.getEmployeeCode());

	        employeeTable.addCell("Employee Name");
	        employeeTable.addCell(payslip.getEmployeeName());

	        employeeTable.addCell("Department");
	        employeeTable.addCell(payslip.getDepartment());

	        employeeTable.addCell("Designation");
	        employeeTable.addCell(payslip.getDesignation());

	        document.add(employeeTable);

	        document.add(new Paragraph(" "));

	        // Earnings & Deductions
	        Table salaryTable = new Table(2);

	        salaryTable.addHeaderCell("Earnings");
	        salaryTable.addHeaderCell("Amount");

	        salaryTable.addCell("Basic");
	        salaryTable.addCell(payslip.getBasicSalary().toString());

	        salaryTable.addCell("HRA");
	        salaryTable.addCell(payslip.getHra().toString());

	        salaryTable.addCell("DA");
	        salaryTable.addCell(payslip.getDa().toString());

	        salaryTable.addCell("Special Allowance");
	        salaryTable.addCell(payslip.getSpecialAllowance().toString());

	        salaryTable.addCell("Other Allowance");
	        salaryTable.addCell(payslip.getOtherAllowance().toString());

	        salaryTable.addCell("Overtime");
	        salaryTable.addCell(payslip.getOvertimeAmount().toString());

	        salaryTable.addCell("PF");
	        salaryTable.addCell(payslip.getPf().toString());

	        salaryTable.addCell("Professional Tax");
	        salaryTable.addCell(payslip.getProfessionalTax().toString());

	        salaryTable.addCell("Other Deduction");
	        salaryTable.addCell(payslip.getOtherDeduction().toString());

	        salaryTable.addCell("LOP Deduction");
	        salaryTable.addCell(payslip.getLopDeduction().toString());

	        document.add(salaryTable);

	        document.add(new Paragraph(" "));

	        document.add(new Paragraph(
	                "Gross Salary : " + payslip.getGrossSalary())
	        		.setFont(boldFont));

	        document.add(new Paragraph(
	                "Total Deduction : " + payslip.getTotalDeduction())
	        		.setFont(boldFont));

	        document.add(new Paragraph(
	                "Net Salary : " + payslip.getNetSalary())
	        		.setFont(boldFont)
	                .setFontSize(15));

	        document.close();

	        return outputStream.toByteArray();

	    } catch (Exception e) {

	        throw new RuntimeException(
	                "Unable to generate payslip PDF", e);

	    }
	}

	@Override
	public PayrollDashboardResponse getDashboard(Integer month, Integer year) {
		// TODO Auto-generated method stub
		return null;
	}
	
    
    
    
    
}
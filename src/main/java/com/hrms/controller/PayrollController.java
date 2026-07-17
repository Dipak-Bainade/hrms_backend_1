package com.hrms.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.payroll.PayslipResponse;
import com.hrms.dto.response.ApiResponse;
import com.hrms.dto.response.PayrollDashboardResponse;
import com.hrms.service.PayrollService;
import com.hrms.service.PayslipService;
import com.hrms.dto.request.PayrollRequest;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {
	
	

	private final PayrollService payrollService;
	
	
	@GetMapping("/{id}/payslip")
	@PreAuthorize("hasAuthority('PAYROLL_VIEW')")
	public ResponseEntity<ApiResponse<PayslipResponse>>
	getPayslip(
	        @PathVariable Long id) {

	    return ResponseEntity.ok(

	            ApiResponse.<PayslipResponse>builder()

	                    .success(true)

	                    .message("Payslip generated successfully.")

	                    .data(
	                            payrollService
	                                    .generatePayslip(id))

	                    .build());

	}
	
	
	@GetMapping("/{id}/payslip/download")
	@PreAuthorize("hasAuthority('PAYROLL_VIEW')")
	public ResponseEntity<byte[]> downloadPayslip(
	        @PathVariable Long id) {

	    byte[] pdf = payrollService.downloadPayslip(id);

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "inline; filename=payslip.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdf);
	}
	
	@PostMapping("/process")
	@PreAuthorize("hasAuthority('PAYROLL_PROCESS')")
	public ResponseEntity<ApiResponse<String>> processPayroll(
	        @RequestBody PayrollRequest request) {

	    payrollService.processPayroll(request);

	    return ResponseEntity.ok(
	            ApiResponse.<String>builder()
	                    .success(true)
	                    .message("Payroll processed successfully.")
	                    .data("Success")
	                    .build()
	    );
	}
	
	@GetMapping("/dashboard")
	@PreAuthorize("hasAuthority('PAYROLL_VIEW')")
	public ResponseEntity<ApiResponse<PayrollDashboardResponse>>
	dashboard(

	        @RequestParam Integer month,

	        @RequestParam Integer year) {

	    return ResponseEntity.ok(

	            ApiResponse.<PayrollDashboardResponse>builder()

	                    .success(true)

	                    .message("Payroll dashboard fetched successfully.")

	                    .data(
	                            payrollService.getDashboard(
	                                    month,
	                                    year))

	                    .build());

	}
}

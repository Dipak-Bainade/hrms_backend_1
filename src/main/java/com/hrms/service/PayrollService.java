package com.hrms.service;

import java.util.List;

import com.hrms.dto.payroll.PayslipResponse;
import com.hrms.dto.request.PayrollRequest;
import com.hrms.dto.response.PayrollDashboardResponse;
import com.hrms.dto.response.PayrollResponse;

public interface PayrollService {

    PayrollResponse processPayroll(
            PayrollRequest request);

    PayrollResponse reprocessPayroll(
            PayrollRequest request);

    PayrollResponse getPayroll(
            Long employeeId,
            Integer month,
            Integer year);

    List<PayrollResponse>
    getPayrollHistory(
            Long employeeId);
    
    PayslipResponse generatePayslip(Long payrollId);
    
    byte[] downloadPayslip(Long payrollId);
    
    PayrollDashboardResponse getDashboard(
            Integer month,
            Integer year);

}

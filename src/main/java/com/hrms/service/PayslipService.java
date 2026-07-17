package com.hrms.service;

import com.hrms.dto.payroll.PayslipResponse;

public interface PayslipService {

    PayslipResponse generatePayslip(
            Long payrollId);

    byte[] generatePdf(
            Long payrollId);

    void emailPayslip(
            Long payrollId);

}
package com.hrms.payroll;

import org.springframework.stereotype.Component;

import com.hrms.dto.payroll.AttendanceSummary;
import com.hrms.dto.payroll.PayrollCalculationResult;
import com.hrms.entity.SalaryStructure;

@Component
public class SalaryCalculator {

    public PayrollCalculationResult calculate(

            SalaryStructure salary,

            AttendanceSummary attendance) {

        double gross =
                calculateGross(salary);

        double deduction =
                calculateDeduction(salary);

        double lop =
                calculateLop(
                        gross,
                        attendance);

        double overtime =
                calculateOvertime(
                        attendance);

        double net =
                gross
                        + overtime
                        - deduction
                        - lop;

        return PayrollCalculationResult
                .builder()

                .grossSalary(gross)

                .allowance(
                        gross
                                - salary.getBasicSalary())

                .deduction(deduction)

                .lopDeduction(lop)

                .overtimeAmount(overtime)

                .netSalary(net)

                .build();

    }
    
    private double calculateGross(
            SalaryStructure salary) {

        return defaultZero(salary.getBasicSalary())
                + defaultZero(salary.getHra())
                + defaultZero(salary.getDa())
                + defaultZero(salary.getSpecialAllowance())
                + defaultZero(salary.getOtherAllowance());

    }
    
    private double calculateDeduction(
            SalaryStructure salary) {

        return defaultZero(salary.getPf())
                + defaultZero(salary.getProfessionalTax())
                + defaultZero(salary.getIncomeTax());

    }

    
    private double calculateLop(
            double gross,
            AttendanceSummary attendance) {

        if (attendance.getWorkingDays() == null
                || attendance.getWorkingDays() == 0) {
            return 0.0;
        }

        return (gross / attendance.getWorkingDays())
                * defaultZero(attendance.getLopDays());

    }
    
    private double calculateOvertime(
            AttendanceSummary attendance) {

        // Initial implementation
        // Example: ₹200 per overtime hour

        return defaultZero(attendance.getOvertimeHours()) * 200;

    }
    
    private double defaultZero(Double value) {

        return value == null ? 0.0 : value;

    }
    
    private double defaultZero(Integer value) {

        return value == null ? 0.0 : value.doubleValue();

    }
}

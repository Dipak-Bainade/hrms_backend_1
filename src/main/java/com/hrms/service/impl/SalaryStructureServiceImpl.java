package com.hrms.service.impl;

import com.hrms.dto.request.SalaryStructureRequest;
import com.hrms.dto.response.SalaryStructureResponse;
import com.hrms.entity.SalaryStructure;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.repository.SalaryStructureRepository;
import com.hrms.service.SalaryStructureService;

public class SalaryStructureServiceImpl implements SalaryStructureService{

	
	private Double calculateGross(
	        SalaryStructure salary) {

	    return salary.getBasicSalary()

	            + defaultZero(salary.getHra())

	            + defaultZero(salary.getDa())

	            + defaultZero(salary.getSpecialAllowance())

	            + defaultZero(salary.getOtherAllowance());

	}
	
	private Double calculateDeduction(
	        SalaryStructure salary) {

	    return defaultZero(salary.getPf())

	            + defaultZero(salary.getProfessionalTax())

	            + defaultZero(salary.getOtherDeduction());

	}
	
	private Double calculateNetSalary(
	        SalaryStructure salary) {

	    return calculateGross(salary)

	            - calculateDeduction(salary);

	}
	
	private Double defaultZero(
	        Double value) {

	    return value == null ? 0.0 : value;

	}
	
	


	@Override
	public SalaryStructureResponse createSalaryStructure(SalaryStructureRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
}

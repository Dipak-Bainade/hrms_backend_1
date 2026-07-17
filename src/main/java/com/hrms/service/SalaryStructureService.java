package com.hrms.service;

import com.hrms.dto.request.SalaryStructureRequest;
import com.hrms.dto.response.SalaryStructureResponse;

public interface SalaryStructureService {
	
    SalaryStructureResponse createSalaryStructure(
            SalaryStructureRequest request);

}

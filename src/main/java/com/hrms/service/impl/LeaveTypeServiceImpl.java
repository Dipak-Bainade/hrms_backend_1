package com.hrms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hrms.dto.request.LeaveTypeRequest;
import com.hrms.dto.response.LeaveTypeResponse;
import com.hrms.entity.Company;
import com.hrms.entity.LeaveType;
import com.hrms.exception.DuplicateResourceException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.CompanyRepository;
import com.hrms.repository.LeaveTypeRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.LeaveBalanceInitializerService;
import com.hrms.service.LeaveTypeService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService{
	
    private final LeaveTypeRepository leaveTypeRepository;

    private final CompanyRepository companyRepository;

    private final CurrentUser currentUser;

    private final LeaveBalanceInitializerService leaveBalanceInitializerService;
    
    @Override
    public LeaveTypeResponse createLeaveType(
            LeaveTypeRequest request) {

        Long companyId = currentUser.getCompanyId();

        validateDuplicateLeaveType(
                companyId,
                request.getLeaveName());

        Company company = companyRepository
                .findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found."));

        LeaveType leaveType = new LeaveType();

        leaveType.setLeaveName(request.getLeaveName());

        leaveType.setDescription(request.getDescription());

        leaveType.setDefaultDays(request.getDefaultDays());

        leaveType.setPaidLeave(request.getPaidLeave());

        leaveType.setCompany(company);

        leaveType.setActive(true);

        leaveType = leaveTypeRepository.save(leaveType);
        
        leaveBalanceInitializerService.initializeForLeaveType(leaveType);

        return mapToResponse(leaveType);

    }
    
    private void validateDuplicateLeaveType(
            Long companyId,
            String leaveName) {

        if (leaveTypeRepository
                .existsByCompanyIdAndLeaveNameIgnoreCase(
                        companyId,
                        leaveName)) {

            throw new DuplicateResourceException(
                    "Leave type already exists.");

        }

    }
    
    private LeaveTypeResponse mapToResponse(
            LeaveType leaveType) {

        return LeaveTypeResponse.builder()

                .id(leaveType.getId())

                .leaveName(leaveType.getLeaveName())

                .description(leaveType.getDescription())

                .defaultDays(leaveType.getDefaultDays())

                .paidLeave(leaveType.getPaidLeave())

                .active(leaveType.getActive())

                .build();

    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LeaveTypeResponse> getAllLeaveTypes() {

        Long companyId = currentUser.getCompanyId();

        return leaveTypeRepository
                .findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }
    
    @Override
    @Transactional(readOnly = true)
    public LeaveTypeResponse getLeaveTypeById(Long id) {

        Long companyId = currentUser.getCompanyId();

        LeaveType leaveType = leaveTypeRepository
                .findByIdAndCompanyId(id, companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Leave type not found."));

        return mapToResponse(leaveType);

    }
    
    @Override
    public LeaveTypeResponse updateLeaveType(
            Long id,
            LeaveTypeRequest request) {

        Long companyId = currentUser.getCompanyId();

        LeaveType leaveType = leaveTypeRepository
                .findByIdAndCompanyId(id, companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Leave type not found."));

        if (!leaveType.getLeaveName()
                .equalsIgnoreCase(request.getLeaveName())) {

            validateDuplicateLeaveType(
                    companyId,
                    request.getLeaveName());
        }

        leaveType.setLeaveName(request.getLeaveName());
        leaveType.setDescription(request.getDescription());
        leaveType.setDefaultDays(request.getDefaultDays());
        leaveType.setPaidLeave(request.getPaidLeave());

        leaveTypeRepository.save(leaveType);

        return mapToResponse(leaveType);

    }
    
    @Override
    public void deleteLeaveType(Long id) {

        Long companyId = currentUser.getCompanyId();

        LeaveType leaveType = leaveTypeRepository
                .findByIdAndCompanyId(id, companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Leave type not found."));

        leaveType.setActive(false);

        leaveTypeRepository.save(leaveType);

    }
    
    
    
}

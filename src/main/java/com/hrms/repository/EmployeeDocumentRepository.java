package com.hrms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.EmployeeDocument;
import com.hrms.enums.DocumentType;

public interface EmployeeDocumentRepository
        extends JpaRepository<EmployeeDocument, Long> {

    List<EmployeeDocument>

    findByEmployeeIdAndCompanyIdAndActiveTrue(

            Long employeeId,

            Long companyId);
    
    Optional<EmployeeDocument> findByIdAndCompanyIdAndActiveTrue(
            Long id,
            Long companyId);

    List<EmployeeDocument> findByEmployeeIdAndCompanyIdAndActiveTrueOrderByUploadedAtDesc(
            Long employeeId,
            Long companyId);
    
    Optional<EmployeeDocument>

    findTopByEmployeeIdAndDocumentTypeAndCompanyIdOrderByVersionDesc(

            Long employeeId,

            DocumentType documentType,

            Long companyId);

}
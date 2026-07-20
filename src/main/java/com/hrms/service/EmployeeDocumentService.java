package com.hrms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hrms.dto.request.DocumentVerificationRequest;
import com.hrms.dto.request.EmployeeDocumentRequest;
import com.hrms.dto.response.EmployeeDocumentResponse;

import org.springframework.core.io.Resource;

public interface EmployeeDocumentService {

    EmployeeDocumentResponse uploadDocument(
            MultipartFile file,
            EmployeeDocumentRequest request);
    
    EmployeeDocumentResponse getDocument(Long documentId);

    List<EmployeeDocumentResponse> getEmployeeDocuments(Long employeeId);

    Resource downloadDocument(Long documentId);
    
    EmployeeDocumentResponse verifyDocument(

            Long documentId,

            DocumentVerificationRequest request);

}

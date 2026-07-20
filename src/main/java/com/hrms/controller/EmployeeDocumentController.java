package com.hrms.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hrms.dto.request.DocumentVerificationRequest;
import com.hrms.dto.request.EmployeeDocumentRequest;
import com.hrms.dto.response.ApiResponse;
import com.hrms.dto.response.EmployeeDocumentResponse;
import com.hrms.service.EmployeeDocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee-documents")
@RequiredArgsConstructor
public class EmployeeDocumentController {

    private final EmployeeDocumentService service;

    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    @PreAuthorize("hasAuthority('DOCUMENT_CREATE')")

    public ResponseEntity<ApiResponse<EmployeeDocumentResponse>>
    upload(

            @RequestPart("file")
            MultipartFile file,

            @RequestPart("request")
            EmployeeDocumentRequest request) {

        return ResponseEntity.ok(

                ApiResponse.<EmployeeDocumentResponse>builder()

                        .success(true)

                        .message("Document uploaded successfully.")

                        .data(service.uploadDocument(file, request))

                        .build());

    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCUMENT_VIEW')")
    public ResponseEntity<ApiResponse<EmployeeDocumentResponse>>
    getDocument(
            @PathVariable Long id) {

        return ResponseEntity.ok(

                ApiResponse.<EmployeeDocumentResponse>builder()

                        .success(true)

                        .message("Document fetched successfully.")

                        .data(service.getDocument(id))

                        .build());

    }
    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('DOCUMENT_VIEW')")
    public ResponseEntity<ApiResponse<List<EmployeeDocumentResponse>>>
    employeeDocuments(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(

                ApiResponse.<List<EmployeeDocumentResponse>>builder()

                        .success(true)

                        .message("Employee documents fetched successfully.")

                        .data(
                                service.getEmployeeDocuments(employeeId))

                        .build());

    }
    
    @GetMapping("/{id}/download")
    @PreAuthorize("hasAuthority('DOCUMENT_VIEW')")
    public ResponseEntity<Resource> download(
            @PathVariable Long id) {

        Resource resource =
                service.downloadDocument(id);

        return ResponseEntity.ok()

                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM)

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + resource.getFilename()
                                + "\"")

                .body(resource);

    }
    
    
    @PutMapping("/{id}/verify")

    @PreAuthorize("hasAuthority('DOCUMENT_VERIFY')")

    public ResponseEntity<ApiResponse<EmployeeDocumentResponse>>

    verify(

            @PathVariable Long id,

            @RequestBody

            DocumentVerificationRequest request) {

        return ResponseEntity.ok(

                ApiResponse

                .<EmployeeDocumentResponse>builder()

                        .success(true)

                        .message(

                                "Document verified successfully.")

                        .data(

                                service.verifyDocument(

                                        id,

                                        request))

                        .build());

    }
    
    
    
    

}

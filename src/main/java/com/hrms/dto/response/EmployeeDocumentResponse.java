package com.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hrms.enums.DocumentType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeDocumentResponse {

    private Long id;

    private Long employeeId;

    private String employeeCode;

    private String employeeName;

    private DocumentType documentType;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    private Integer version;

    private LocalDate expiryDate;

    private Boolean verified;

    private LocalDateTime uploadedAt;

}

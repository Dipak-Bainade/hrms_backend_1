package com.hrms.dto.request;

import java.time.LocalDate;

import com.hrms.enums.DocumentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDocumentRequest {

    private Long employeeId;

    private DocumentType documentType;

    private LocalDate expiryDate;

}

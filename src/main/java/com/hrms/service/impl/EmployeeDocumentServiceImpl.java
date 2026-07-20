package com.hrms.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hrms.config.FileStorageProperties;
import com.hrms.dto.request.DocumentVerificationRequest;
import com.hrms.dto.request.EmployeeDocumentRequest;
import com.hrms.dto.response.EmployeeDocumentResponse;
import com.hrms.entity.Employee;
import com.hrms.entity.EmployeeDocument;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.EmployeeDocumentRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.UserRepository;
import com.hrms.security.CurrentUser;
import com.hrms.service.EmployeeDocumentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class EmployeeDocumentServiceImpl
        implements EmployeeDocumentService {

    private final CurrentUser currentUser;

    private final EmployeeRepository employeeRepository;

    private final EmployeeDocumentRepository documentRepository;

    private final FileStorageProperties properties;
    
    private final UserRepository userRepository;

    @Override
    public EmployeeDocumentResponse uploadDocument(

            MultipartFile file,

            EmployeeDocumentRequest request) {

        validateFile(file);

        Long companyId =
                currentUser.getCompanyId();

        Employee employee =
                employeeRepository

                        .findByIdAndCompanyId(
                                request.getEmployeeId(),
                                companyId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        String storedName =
                storeFile(file, employee);
        
        Optional<EmployeeDocument> latestVersion =

                documentRepository

                .findTopByEmployeeIdAndDocumentTypeAndCompanyIdOrderByVersionDesc(

                        employee.getId(),

                        request.getDocumentType(),

                        companyId);
        
        

        EmployeeDocument document =
                new EmployeeDocument();

        document.setEmployee(employee);

        document.setCompany(employee.getCompany());

        document.setDocumentType(
                request.getDocumentType());

        document.setOriginalFileName(
                file.getOriginalFilename());

        document.setStoredFileName(
                storedName);

        document.setFilePath(
                buildRelativePath(
                        employee,
                        storedName));

        document.setContentType(
                file.getContentType());

        document.setFileSize(
                file.getSize());

        document.setExpiryDate(
                request.getExpiryDate());

        document.setUploadedAt(
                LocalDateTime.now());

        document.setVersion(1);

        document.setVerified(false);

        document.setActive(true);

        document =
                documentRepository.save(document);

        return mapToResponse(document);

    }
    
    private void validateFile(
            MultipartFile file) {

        if (file.isEmpty()) {

            throw new IllegalArgumentException(
                    "File is required.");

        }

        if (file.getSize() > 10 * 1024 * 1024) {

            throw new IllegalArgumentException(
                    "Maximum file size is 10 MB.");

        }

        String type =
                file.getContentType();

        if (type == null ||
                !(type.equals("application/pdf")
                || type.equals("image/png")
                || type.equals("image/jpeg"))) {

            throw new IllegalArgumentException(
                    "Only PDF, JPG and PNG files are allowed.");

        }

    }
    
    
    private String storeFile(

            MultipartFile file,

            Employee employee) {

        try {

            String extension =
                    getExtension(
                            file.getOriginalFilename());

            String fileName =
                    UUID.randomUUID() + extension;

            Path folder = Paths.get(

                    properties.getUploadDir(),

                    "company-" + employee.getCompany().getId(),

                    "employee-" + employee.getId());

            Files.createDirectories(folder);

            Path destination =
                    folder.resolve(fileName);

            Files.copy(

                    file.getInputStream(),

                    destination,

                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        }

        catch (IOException ex) {

            throw new RuntimeException(
                    "Unable to store document.", ex);

        }

    }
    
    private String getExtension(
            String fileName) {

        int index =
                fileName.lastIndexOf('.');

        if (index == -1) {

            return "";

        }

        return fileName.substring(index);

    }
    
    private String buildRelativePath(

            Employee employee,

            String fileName) {

        return "company-"

                + employee.getCompany().getId()

                + "/employee-"

                + employee.getId()

                + "/"

                + fileName;

    }
    
    private EmployeeDocumentResponse mapToResponse(
            EmployeeDocument document) {

        Employee employee =
                document.getEmployee();

        return EmployeeDocumentResponse.builder()

                .id(document.getId())

                .employeeId(employee.getId())

                .employeeCode(employee.getEmployeeCode())

                .employeeName(
                        employee.getFirstName()
                                + " "
                                + employee.getLastName())

                .documentType(
                        document.getDocumentType())

                .originalFileName(
                        document.getOriginalFileName())

                .contentType(
                        document.getContentType())

                .fileSize(
                        document.getFileSize())

                .version(
                        document.getVersion())

                .expiryDate(
                        document.getExpiryDate())

                .verified(
                        document.getVerified())

                .uploadedAt(
                        document.getUploadedAt())

                .build();

    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDocumentResponse getDocument(Long documentId) {

        Long companyId = currentUser.getCompanyId();

        EmployeeDocument document =
                documentRepository
                        .findByIdAndCompanyIdAndActiveTrue(
                                documentId,
                                companyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found"));

        return mapToResponse(document);

    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDocumentResponse> getEmployeeDocuments(
            Long employeeId) {

        Long companyId = currentUser.getCompanyId();

        employeeRepository
                .findByIdAndCompanyId(
                        employeeId,
                        companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found"));

        return documentRepository

                .findByEmployeeIdAndCompanyIdAndActiveTrueOrderByUploadedAtDesc(
                        employeeId,
                        companyId)

                .stream()

                .map(this::mapToResponse)

                .toList();

    }
    
    @Override
    @Transactional(readOnly = true)
    public Resource downloadDocument(Long documentId) {

        Long companyId = currentUser.getCompanyId();

        EmployeeDocument document =
                documentRepository
                        .findByIdAndCompanyIdAndActiveTrue(
                                documentId,
                                companyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found"));

        try {

            Path path = Paths.get(
                    properties.getUploadDir(),
                    document.getFilePath());

            Resource resource =
                    new UrlResource(path.toUri());

            if (!resource.exists()) {

                throw new RuntimeException(
                        "File not found.");

            }

            return resource;

        } catch (MalformedURLException ex) {

            throw new RuntimeException(
                    "Unable to download file.", ex);

        }

    }

    @Override
    @Transactional
    public EmployeeDocumentResponse verifyDocument(
            Long documentId,
            DocumentVerificationRequest request) {

        Long companyId = currentUser.getCompanyId();

        EmployeeDocument document =
                documentRepository
                        .findByIdAndCompanyIdAndActiveTrue(
                                documentId,
                                companyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found"));

        document.setVerified(
                request.getVerified());

        document.setVerifiedBy(

                userRepository.findById(

                        currentUser.getUserId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found")));

        document.setVerifiedAt(

                LocalDateTime.now());

        document.setVerificationRemarks(

                request.getRemarks());

        document.setVerified(
                request.getVerified());

        document.setVerifiedBy(

                userRepository.findById(

                        currentUser.getUserId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found")));

        document.setVerifiedAt(
                LocalDateTime.now());

        document.setVerificationRemarks(
                request.getRemarks());

        document = documentRepository.save(document);

        return mapToResponse(document);
    }
    
    
}
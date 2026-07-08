package com.hrms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.dto.request.DepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;
import com.hrms.entity.Department;
import com.hrms.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
	

    private final DepartmentService departmentService;


    @PostMapping
    @PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {

        return ResponseEntity.ok(
                departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
    public ResponseEntity<DepartmentResponse> getDepartmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                departmentService.getDepartmentById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request) {

        return ResponseEntity.ok(
                departmentService.updateDepartment(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long id) {

        departmentService.deleteDepartment(id);

        return ResponseEntity.noContent().build();
    }
    
    

}

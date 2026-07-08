package com.hrms.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hrms.entity.Permission;
import com.hrms.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner{
	
	private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {

        List<String> permissions = List.of(

                // Employee
                "EMPLOYEE_CREATE",
                "EMPLOYEE_UPDATE",
                "EMPLOYEE_DELETE",
                "EMPLOYEE_VIEW",

                // Department
                "DEPARTMENT_CREATE",
                "DEPARTMENT_UPDATE",
                "DEPARTMENT_DELETE",
                "DEPARTMENT_VIEW",

                // Designation
                "DESIGNATION_CREATE",
                "DESIGNATION_UPDATE",
                "DESIGNATION_DELETE",
                "DESIGNATION_VIEW",

                // Leave
                "LEAVE_APPLY",
                "LEAVE_APPROVE",

                // Payroll
                "PAYROLL_PROCESS",

                // Attendance
                "ATTENDANCE_MARK",
                "ATTENDANCE_VIEW"
        );

        for (String permissionName : permissions) {

            if (!permissionRepository.existsByPermissionName(permissionName)) {

                Permission permission = new Permission();

                permission.setPermissionName(permissionName);

                permissionRepository.save(permission);

                System.out.println(permissionName + " inserted.");
            }
        }
    }

}

package com.hrms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>{

	Optional<Permission> findByPermissionName(String permissionName);
	
	boolean existsByPermissionName(String permissionName);

	
}

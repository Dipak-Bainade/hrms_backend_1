package com.hrms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Optional<Role> findByRoleName(String roleName);

}

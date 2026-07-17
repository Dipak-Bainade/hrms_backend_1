package com.hrms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.entity.SalaryStructure;

public interface SalaryStructureRepository
extends JpaRepository<SalaryStructure, Long> {

Optional<SalaryStructure> findByIdAndCompanyId(
    Long id,
    Long companyId);

boolean existsByStructureNameAndCompanyId(
    String structureName,
    Long companyId);


}
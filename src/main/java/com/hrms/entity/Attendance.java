package com.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hrms.enums.AttendanceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
	    name = "attendance",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            columnNames = {
	                "employee_id",
	                "attendance_date"
	            }
	        )
	    }
	)
public class Attendance extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private Double workingHours;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @Column(length = 500)
    private String remarks;

    private Boolean active = true;

}

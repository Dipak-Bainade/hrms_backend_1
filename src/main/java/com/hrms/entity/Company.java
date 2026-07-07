package com.hrms.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="companies")
public class Company extends BaseEntity{
	
	@NotBlank(message = "Company name is required")
    @Column(nullable = false, length = 100)
    private String companyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String country;

    @Column(length = 10)
    private String pinCode;

    @Column(length = 100)
    private String website;

    private String logo;

    @Column(length = 30)
    private String subscriptionType;

    private LocalDate subscriptionExpiry;

}

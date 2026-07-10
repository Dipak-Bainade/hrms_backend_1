package com.hrms.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class AuthenticatedUser extends User{
	
    private final Long userId;

    private final Long companyId;

    private final String role;

    public AuthenticatedUser(
            Long userId,
            Long companyId,
            String role,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);

        this.userId = userId;
        this.companyId = companyId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getRole() {
        return role;
    }
	

}

package com.hrms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    private AuthenticatedUser getPrincipal() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof AuthenticatedUser)) {

            throw new RuntimeException("No authenticated user found.");
        }

        return (AuthenticatedUser) authentication.getPrincipal();
    }

    public Long getUserId() {

        return getPrincipal().getUserId();
    }

    public Long getCompanyId() {

        return getPrincipal().getCompanyId();
    }

    public String getRole() {

        return getPrincipal().getRole();
    }

    public String getEmail() {

        return getPrincipal().getUsername();
    }

}

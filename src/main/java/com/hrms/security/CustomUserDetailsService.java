package com.hrms.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hrms.entity.User;
import com.hrms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

	
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

    
        Collection<? extends GrantedAuthority> authorities =
                user.getRole()
                    .getPermissions()
                    .stream()
                    .map(permission ->
                            new SimpleGrantedAuthority(permission.getPermissionName()))
                    .toList();
        
        return new AuthenticatedUser(
                user.getId(),
                user.getCompany().getId(),
                user.getRole().getRoleName(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}

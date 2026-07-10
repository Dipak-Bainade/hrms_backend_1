package com.hrms.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hrms.dto.request.LoginRequest;
import com.hrms.dto.response.LoginResponse;
import com.hrms.entity.User;
import com.hrms.repository.UserRepository;
import com.hrms.security.AuthenticatedUser;
import com.hrms.security.JwtService;
import com.hrms.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtService jwtService;
    
    @Override
    public LoginResponse login(LoginRequest request) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        String token = jwtService.generateToken(authenticatedUser);

        return new LoginResponse(

                token,

                "Bearer",

                user.getId(),

                user.getFirstName() + " " + user.getLastName(),

                user.getEmail(),

                user.getRole().getRoleName(),

                user.getCompany().getId()

        );

    }
}

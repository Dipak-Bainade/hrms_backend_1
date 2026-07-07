package com.hrms.service;

import com.hrms.dto.request.LoginRequest;
import com.hrms.dto.response.LoginResponse;

public interface AuthService {
	
	LoginResponse login(LoginRequest request);

}

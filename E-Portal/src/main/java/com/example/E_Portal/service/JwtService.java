package com.example.E_Portal.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.example.E_Portal.dto.EmployeeDto;

public interface JwtService {
	
	public String generarteToken(EmployeeDto employeeDto);

	public String extractUserEmail(String token);

	public boolean isTokenValid(String jwt, EmployeeDto employeeDto);

	public UsernamePasswordAuthenticationToken getAuthenticationToken(String jwt, Authentication authentication,
			EmployeeDto employeeDto);

}

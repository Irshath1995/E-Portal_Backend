package com.example.e_portal.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.e_portal.dto.EmployeeDto;

public interface EmployeeService {
	
	ResponseEntity<String> uploadImage(Integer empId, MultipartFile empImage);
	
	EmployeeDto addEmployee(EmployeeDto employeeDto);

	List<EmployeeDto> getAllEmployees();

}

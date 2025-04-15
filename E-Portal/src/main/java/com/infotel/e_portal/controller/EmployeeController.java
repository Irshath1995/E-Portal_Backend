package com.infotel.e_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.infotel.e_portal.dto.EmployeeDto;
import com.infotel.e_portal.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	public EmployeeController() {
	}

	@PostMapping("/{empId}/uploadImage")
	public ResponseEntity<String> uploadImage(@PathVariable Integer empId, @RequestParam("empImage") MultipartFile empImage){
		return employeeService.uploadImage(empId, empImage);
	}
	
	@PostMapping("/addEmployee")
	public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody EmployeeDto employeeDto){
		EmployeeDto addEmployee = employeeService.addEmployee(employeeDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(addEmployee);
	}
	
	@GetMapping("/allEmployees")
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
		List<EmployeeDto> employees = employeeService.getAllEmployees();
		return ResponseEntity.status(HttpStatus.OK).body(employees);
	}
	
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
//	public ResponseEntity<Map<String, String>> handleConstraintViolationException(jakarta.validation.ConstraintViolationException ex) {
//	    Map<String, String> errors = new HashMap<>();
//	    
//	    ex.getConstraintViolations().forEach(violation -> {
//	        String fieldName = violation.getPropertyPath().toString();
//	        String errorMessage = violation.getMessage();
//	        errors.put(fieldName, errorMessage);
//	    });
//
//	    return ResponseEntity.badRequest().body(errors);
//	}
	
}

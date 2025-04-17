package com.example.E_Portal.dto;

import java.time.LocalDate;

import com.example.E_Portal.model.Employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmployeeDto {

	@NotNull(message="employee is mandatory.")
//	@Pattern(message="employeeId is not valid", regexp="^[0-9]+$")
	private Integer empId;
	
	@NotBlank(message="firstName is mandatory.")
	@Pattern(message="firstName is not valid", regexp="^[a-zA-Z]+( [A-Za-z]+)*$")
	private String firstName;
	
	@NotBlank(message="lastName is mandatory.")
	@Pattern(message="lastName is not valid", regexp="^[a-zA-Z]+( [A-Za-z]+)*$")
	private String lastName;
	
	@NotBlank(message="email is mandatory.")
	@Email(message="email is invalid", regexp = "^[a-z]+\\.[a-z]+@infotel\\.com$")
	private String email;
	
	@NotNull(message="dateOfJoining is mandatory.")
	private LocalDate dateOfJoining;
	
	private LocalDate lastWorkingDay;
	
	@NotNull(message="dateOfBirth is mandatory.")
	private LocalDate dateOfBirth;
	
	@NotBlank(message="gender is mandatory.")
	private String gender;
	
	@NotNull(message="totalYearsOfExperience is mandatory.")
	private Integer totalYearsOfExperience;
	
	@NotBlank(message="workLocation is mandatory.")
	@Pattern( message="work location is not valid", regexp="^[a-zA-Z]+(\\s[A-Za-z]+)*$")
	private String workLocation;
	
	@NotBlank(message="currentAddress is mandatory.")
	@Pattern(message="Current address is not valid", regexp="^[0-9A-Za-z][0-9A-Za-z \\-,.!@#$%^&*()_/]*[0-9A-Za-z]$")
	private String currentAddress;
	
	@NotBlank(message="permanentAddress is mandatory.")
	@Pattern(message="Current address is not valid", regexp="^[0-9A-Za-z][0-9A-Za-z \\-,.!@#$%^&*()_/]*[0-9A-Za-z]$")
	private String permanentAddress;
	
	@NotBlank(message="region is mandatory.")
	@Pattern(message="region is not valid", regexp="^[a-zA-Z]+( [A-Za-z]+)*$")
	private String region;
	
	@NotBlank(message="contactNumber is mandatory.")
	@Pattern(message="contactNumber is not valid", regexp="^[0-9]+$")
	private String contactNumber;
	
	@NotBlank(message="emergencyContactPerson is mandatory.")
	@Pattern(message="emergencyContactPerson is not valid", regexp="^[a-zA-Z]+( [A-Za-z]+)*$")
	private String emergencyContactPerson;
	
	@NotBlank(message="emergencyContactPersonRelationship is mandatory.")
	@Pattern(message="emergencyContactPersonRelationship is not valid", regexp="^[a-zA-Z]+( [A-Za-z]+)*$")
	private String emergencyContactPersonRelationship;
	
	@NotBlank(message="emergencyNumber is mandatory.")
	@Pattern(message="emergencyNumber is not valid", regexp="^[0-9]+$")
	private String emergencyNumber;
	
	private Boolean isActive = true;
	
	@NotNull(message="managerId is mandatory.")
	private Integer managerId;
	
	@NotBlank(message="initial is mandatory.")
	private String initial;
	
	private String imageUrl;
	
	@NotBlank(message="designation is mandatory.")
	private String designation;
 
	public static EmployeeDto toDTO(Employee employee) {
 
		if (employee == null) {
			return null;
		}
 
		EmployeeDto EmployeeDto = new EmployeeDto();
		EmployeeDto.setEmpId(employee.getEmpId());
		EmployeeDto.setFirstName(employee.getFirstName());
		EmployeeDto.setLastName(employee.getLastName());
		EmployeeDto.setEmail(employee.getEmail());
		EmployeeDto.setContactNumber(employee.getContactNumber());
		EmployeeDto.setGender(employee.getGender());
		EmployeeDto.setTotalYearsOfExperience(employee.getTotalYearsOfExperience());
		EmployeeDto.setWorkLocation(employee.getWorkLocation());
		EmployeeDto.setCurrentAddress(employee.getCurrentAddress());
		EmployeeDto.setPermanentAddress(employee.getPermanentAddress());
		EmployeeDto.setEmergencyContactPerson(employee.getEmergencyContactPerson());
		EmployeeDto.setEmergencyContactPersonRelationship(employee.getEmergencyContactPersonRelationship());
		EmployeeDto.setEmergencyNumber(employee.getEmergencyNumber());
		EmployeeDto.setDateOfBirth(employee.getDateOfBirth());
		EmployeeDto.setDateOfJoining(employee.getDateOfJoining());
		EmployeeDto.setLastWorkingDay(employee.getLastWorkingDay());
		EmployeeDto.setIsActive(employee.getIsActive());
		EmployeeDto.setManagerId(employee.getManagerId());
		EmployeeDto.setRegion(employee.getRegion());
		EmployeeDto.setImageUrl(employee.getImageUrl());
		EmployeeDto.setDesignation(employee.getDesignation());
		EmployeeDto.setInitial(employee.getInitial());
		return EmployeeDto;
 
	}
 
	public static Employee toEntity(EmployeeDto EmployeeDto) {
 
		if (EmployeeDto == null) {
			return null;
		}
 
		Employee employee = new Employee();
		employee.setEmpId(EmployeeDto.getEmpId());
		employee.setFirstName(EmployeeDto.getFirstName());
		employee.setLastName(EmployeeDto.getLastName());
		employee.setEmail(EmployeeDto.getEmail());
		employee.setContactNumber(EmployeeDto.getContactNumber());
		employee.setGender(EmployeeDto.getGender());
		employee.setTotalYearsOfExperience(EmployeeDto.getTotalYearsOfExperience());
		employee.setWorkLocation(EmployeeDto.getWorkLocation());
		employee.setCurrentAddress(EmployeeDto.getCurrentAddress());
		employee.setPermanentAddress(EmployeeDto.getPermanentAddress());
		employee.setEmergencyContactPerson(EmployeeDto.getEmergencyContactPerson());
		employee.setEmergencyContactPersonRelationship(EmployeeDto.getEmergencyContactPersonRelationship());
		employee.setEmergencyNumber(EmployeeDto.getEmergencyNumber());
		employee.setDateOfBirth(EmployeeDto.getDateOfBirth());
		employee.setDateOfJoining(EmployeeDto.getDateOfJoining());
		employee.setLastWorkingDay(EmployeeDto.getLastWorkingDay());
		employee.setIsActive(EmployeeDto.getIsActive());
		employee.setManagerId(EmployeeDto.getManagerId());
		employee.setRegion(EmployeeDto.getRegion());
		employee.setImageUrl(EmployeeDto.getImageUrl());
		employee.setDesignation(EmployeeDto.getDesignation());
		employee.setInitial(EmployeeDto.getInitial());
		return employee;
 
	}
	
//	private static String processInitial(String firstName,String lastName) {
//    	return Character.toUpperCase(firstName.charAt(0))+""+Character.toUpperCase(lastName.charAt(0));
//    }
	
}

package com.example.E_Portal.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(
		name = "employees", 
		uniqueConstraints = {
			@UniqueConstraint(columnNames = { "contact_number" }, name = "contact_number"),
			@UniqueConstraint(columnNames = { "email" }, name = "email") 
		}
)
public class Employee {

	@Id
	@Column(name = "emp_id", nullable = false)
	private Integer empId;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "date_of_joining", nullable = false)
	private LocalDate dateOfJoining;

	@Column(name = "last_working_day")
	private LocalDate lastWorkingDay;

	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "total_years_of_experience", nullable = false)
	private Integer totalYearsOfExperience;

	@Column(name = "region", nullable = false)
	private String region;

	@Column(name = "work_location", nullable = false)
	private String workLocation;

	@Column(name = "current_address", nullable = false)
	private String currentAddress;

	@Column(name = "permanent_address", nullable = false)
	private String permanentAddress;

	@Column(name = "contact_number", unique = true, nullable = false)
	private String contactNumber;

	@Column(name = "emergency_contact_person", nullable = false)
	private String emergencyContactPerson;

	@Column(name = "emergency_contact_person_relationship", nullable = false)
	private String emergencyContactPersonRelationship;

	@Column(name = "emergency_number", nullable = false)
	private String emergencyNumber;

	@Column(name = "emp_isactive", nullable = false)
	private Boolean isActive = true;

	@Column(name = "emp_designation", nullable = false)
	private String designation;

	@Column(name = "manager_id", nullable = false)
	private Integer managerId;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "initial", nullable = false)
	private String initial;

}

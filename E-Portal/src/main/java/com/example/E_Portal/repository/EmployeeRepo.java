package com.example.E_Portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.E_Portal.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer>{

	
	
}

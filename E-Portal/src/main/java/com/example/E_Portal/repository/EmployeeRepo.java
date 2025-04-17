package com.example.e_portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.e_portal.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer>{

	Optional<Employee> findByEmail(String email);
	
}

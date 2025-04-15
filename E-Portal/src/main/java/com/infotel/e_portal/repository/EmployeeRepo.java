package com.infotel.e_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infotel.e_portal.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer>{

	
	
}

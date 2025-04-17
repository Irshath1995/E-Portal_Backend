package com.infotel.e_portal.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotel.e_portal.dto.EmployeeDto;
import com.infotel.e_portal.model.Employee;
import com.infotel.e_portal.model.User;
import com.infotel.e_portal.repository.EmployeeRepo;
import com.infotel.e_portal.repository.UserRepo;
import com.infotel.e_portal.service.JwtService;

@Service
public class UserServiceImpl {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private JwtService jwtService;
	
	public UserServiceImpl(
			UserRepo userRepo,
	        EmployeeRepo employeeRepo
	    ) {
	        this.userRepo = userRepo;
	        this.employeeRepo = employeeRepo;
	    }

	public String checkUserIsValid(User user) {
		System.out.println(user.getEmpId() + " - " + user.getEmail() + " - " + user.getPassword());
		Optional<User> isUser = userRepo.findByEmail(user.getEmail());

		if (isUser.isPresent()) {
			User existingUser = isUser.get();
			if (existingUser.getEmail().equals(user.getEmail())
					&& existingUser.getPassword().equals(user.getPassword())) {
				Optional<Employee> employee = employeeRepo.findById(existingUser.getEmpId());
				EmployeeDto employeeDto = EmployeeDto.toDTO(employee.get());
				return jwtService.generarteToken(employeeDto);
			}
		}
		
		return "";
	}

}

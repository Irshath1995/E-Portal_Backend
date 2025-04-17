package com.example.E_Portal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.E_Portal.model.User;
import com.example.E_Portal.serviceImpl.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class userController {
	
	@Autowired
	private UserServiceImpl userService;
	
	//generate csrf token
//	@GetMapping("/csrfToken")
//	public CsrfToken getCsrfToken(HttpServletRequest request) {
//		return (CsrfToken) request.getAttribute("_csrf");  // _csrf is the attribute name to get the csrfToken.
//	}
	
	@PostMapping("/login")
	public ResponseEntity<?> isUserValid(@RequestBody User user){
		try {
	        String token = userService.checkUserIsValid(user);
	        if (token != null && !token.isEmpty()) {
	            Map<String, String> response = new HashMap<>();
	            response.put("token", token);
	            return ResponseEntity.ok(response);
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + e.getMessage());
	    }
	}

}

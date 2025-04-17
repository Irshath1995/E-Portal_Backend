package com.example.e_portal.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.e_portal.dto.EmployeeDto;
import com.example.e_portal.model.Employee;
import com.example.e_portal.repository.EmployeeRepo;
import com.example.e_portal.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	private final HandlerExceptionResolver handlerExceptionResolver;
	
	public JwtAuthenticationFilter(
			JwtService jwtservice,
			EmployeeRepo employeeRepo,
			HandlerExceptionResolver handlerExceptionResolver
	) {
		this.jwtService = jwtservice;
		this.employeeRepo = employeeRepo;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String path = request.getRequestURI();
		System.out.println("path - " + path);
	    
	    final String authHeader = request.getHeader("Authorization");
	    
//	    If the request is for login, or no Authorization header is present, or it doesn't start with "Bearer"
	    if (path.equals("/user/login") || authHeader == null || !authHeader.startsWith("Bearer ")) { 
            filterChain.doFilter(request, response);
            return;
        }

	    try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUserEmail(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (userEmail != null && authentication == null) {
                Optional<Employee> employeeOpt = employeeRepo.findByEmail(userEmail);
                if (employeeOpt.isPresent()) {
                    EmployeeDto employeeDto = EmployeeDto.toDTO(employeeOpt.get());

                    if (jwtService.isTokenValid(jwt, employeeDto)) {
                        UsernamePasswordAuthenticationToken authToken =
                                jwtService.getAuthenticationToken(jwt, authentication, employeeDto);

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
	}

}

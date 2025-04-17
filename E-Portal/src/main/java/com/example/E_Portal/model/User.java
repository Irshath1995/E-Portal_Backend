package com.example.E_Portal.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@Column(name = "emp_id")
	private Integer empId;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;
	
	private transient Collection<? extends GrantedAuthority> authorities;
	
	public User() {
		
	}

	public User(String userEmail, String password, Collection<? extends GrantedAuthority> authorities) {
		this.email = userEmail;
		this.password = password;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

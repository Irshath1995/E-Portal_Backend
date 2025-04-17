package com.infotel.e_portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.infotel.e_portal.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String email);
	
}

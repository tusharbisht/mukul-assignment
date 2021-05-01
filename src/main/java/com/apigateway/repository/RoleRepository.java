package com.apigateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apigateway.beans.ERole;
import com.apigateway.beans.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
	Optional<Role> findByName(String name);
}

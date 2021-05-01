package com.apigateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apigateway.beans.Driver;



@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
	public Optional<Driver> findById(String Id); 
	public boolean existsById(String Id);
}

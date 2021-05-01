package com.apigateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apigateway.beans.Customer;



@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
	public Optional<Customer> findById(String Id); 
	public boolean existsById(String Id);
}

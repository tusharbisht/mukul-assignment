package com.apigateway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apigateway.beans.Ride;



@Repository
public interface RideRepository extends JpaRepository<Ride, String> {
	public Optional<Ride> findById(Long Id); 
	public boolean existsById(Long Id);
	public List<Ride> findAllById(Long Id);

}

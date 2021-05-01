package com.apigateway.beans;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="rides")
public class Ride{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long id;
	
	@Column(name = "customerid")
	public Long customerid;
	
	@Column(name = "driverid")
	public Long driverid;
	
	 @Column(name = "from")
	 public String from;
	 
	 @Column(name = "to")
	 public String to;
	 
	 @Column(name = "phone")
	 public String phone;

	 //if open set accept for driver
	 @Column(name ="status")
	 public String status;

	public Ride(Long id, Long customerid, Long driverid, String from, String to, String phone, String status) {
		super();
		this.id = id;
		this.customerid = customerid;
		this.driverid = driverid;
		this.from = from;
		this.to = to;
		this.phone = phone;
		this.status = status;
	}
	 
	
	 
	 
	
}

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
@Table(name="drivers")
public class Driver{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long id;
	
	 @Column(name = "name")
	 public String name;
	 
	 @Column(name = "phone")
	 public String phone; 
	 
	 public Driver() {}


	    public Driver(long id, String name, String phone) {
	        this.id = id;
	        this.phone = phone;
	        
	    }
	
}

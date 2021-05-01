package com.apigateway.beans;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;



//@Document(collection = "customers")
@Getter
@Setter
@Entity
@Table(name="customers")
public class Customer{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long id;
	
	 @Column(name = "name")
	 public String name;
	 
	 public Customer() {}


	    public Customer(long id, String name) {
	        this.id = id;
	        this.name = name;
	        
	    }
	
}

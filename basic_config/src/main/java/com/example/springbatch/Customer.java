package com.example.springbatch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Customer {

	@Id
	@GeneratedValue
	private Long id;
	private String firstname;
	private String lastname;
	private String birthdate;
	
}

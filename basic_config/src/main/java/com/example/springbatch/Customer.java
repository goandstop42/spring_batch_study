package com.example.springbatch;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

	private Long id;
	private String firstName;
	private String lastName;
	private String birthDate;
	
}

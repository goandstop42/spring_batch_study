package com.example.springbatch;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

	private Long id;
	private String name;
	private int age;
	
}

package com.example.springbatch;

import java.util.Optional;

import lombok.Data;

@Data
public class Customer {

	private String id;
	private String name;
	private int age;
	private Optional<String> year = Optional.empty();
	
}

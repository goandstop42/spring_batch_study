package com.example.springbatch;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Customer2 {

	@Id
	private Long id;
	private String firstName;
	private String lastName;
	private String birthdate;
	
}

package com.example.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Taco {

	@NotNull
	@Size(min=5, message="Name must be atleast 5 characters long")
	private String name;
	
	@Size(min=1, message="You must choose atleast one ingredient")
	private List<String> ingredients;
}

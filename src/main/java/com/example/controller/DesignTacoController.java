package com.example.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


import com.example.dto.Ingredient;
import com.example.dto.Taco;
import com.example.dto.Ingredient.Type;
import com.example.dto.Order;
import com.example.repository.IngredientRepository;
import com.example.repository.IngredientRepositoryImpl;
import com.example.repository.TacoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

	
	private final IngredientRepository ingredientRepository;
	
	private TacoRepository tacoDesignRepository;
	
	//Create a order object and tie the taco to that order
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}
	
	  @ModelAttribute(name = "design")
	  public Taco taco() {
	    return new Taco();
	  }
	  
	@Autowired
	public DesignTacoController(
			IngredientRepositoryImpl ingredientRepository,
			TacoRepository tacoRepository ) {
		super();
		this.ingredientRepository = ingredientRepository;
		this.tacoDesignRepository = tacoRepository;
	}

	
	@GetMapping
	public String showDesignForm(Model model) {
	
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredientRepository.findAll().forEach(i-> ingredients.add(i));;
		
		Type[] types = Ingredient.Type.values();
		
		for(Type type:types) {
			model.addAttribute(type.toString().toLowerCase(),filterByType(ingredients, type));
			
		}
		//model.addAttribute("design", new Taco());
	    return "design";
		
	
		
	}
	
	public List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
		return ingredients.stream().filter((item)->item.getType().equals(type))
				.collect(Collectors.toList());
	}
	
	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors,
			@ModelAttribute Order order) {
		
		if(errors.hasErrors()) {
			System.out.println(errors.getFieldError());
			return "design";
		}
		
		Taco saved = tacoDesignRepository.save(design);
		
		order.addDesign(saved);
		
		//Save the taco design
		log.info("processing design " + design);
		return "redirect:/orders/current";
	}
}

package com.example.controller;

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
import org.springframework.web.bind.support.SessionStatus;

import com.example.dto.Order;
import com.example.repository.OrderRepository;
import com.example.repository.OrderRepositoryImpl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

	private OrderRepository orderRepository;
	
	@Autowired
	public OrderController(OrderRepositoryImpl orderRepo) {
	    this.orderRepository = orderRepo;
	}
	
	@GetMapping("/current")
	public String orderForm(Model model) {
//		model.addAttribute("order", new Order());
		return "orderForm";
		
	}
	
	@PostMapping
	public String processOrder(@Valid Order order, Errors errors,
			SessionStatus sessionStatus) {
		
		System.out.println(order);
		if(errors.hasErrors()) 
		{
			System.out.println(errors);
			return "orderForm";
		}
		orderRepository.save(order);
		sessionStatus.setComplete();
		
		log.info("Order Submitted" + order.toString());
		
		return "redirect:/";
	}
}

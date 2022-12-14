package com.fuel.orderservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fuel.orderservice.dto.ScheduleDTO;
import com.fuel.orderservice.model.Order;
import com.fuel.orderservice.service.OrderService;
import com.fuel.orderservice.service.Producer;

@RestController
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	Producer producer;

	@PostMapping("/orders")
	public Order addOrder(@RequestBody Order o) {	
		Order order = orderService.addOrder(o);
		producer.publishToAllocationTopic(order);
		return order;
	}
	
	@GetMapping("/orders/{id}")
	public Order getOrder(@PathVariable String id) {
		Order order = orderService.getOrder(id);
		return order;
	}
	
	@GetMapping("/orders")
	public List<Order> viewAllOrders() {
		List<Order> orders = orderService.viewAllOrders();
		return orders;
	}
	
	@GetMapping("/orders/status/{id}")
	public String getOrderStatus(@PathVariable String id) {
		String status = orderService.getOrderStatus(id);
		return status;
	}
	
	@GetMapping("/orders/getMyOrders/{passport}")
	public List<Order> getAllMyOrders(@PathVariable String passport) {
		List<Order> orders = orderService.viewAllMyOrders(passport);
		return orders;
	}
	
	@PutMapping("/orders/receivedConfirm")
	public String  receivedConfirm(@RequestBody Order o) {
		String id = o.getId();
		String result = orderService.receivedConfirm(id);
		return result;		
	}
	
	public void changeAllocationStatusToAllocate(Order currentOrder) {
		Order order = orderService.changeAllocationStatus(currentOrder);
		producer.publishToScheduleTopic(order);
	}

	public void changeAllocationStatusToReject(Order currentOrder) {
		orderService.changeAllocationStatus(currentOrder);
	}
	
	public void addScheduledDate(Order order) {
		ScheduleDTO scheduleDTO  = orderService.addScheduledDate(order);
		producer.publishToDispatchTopic(scheduleDTO);
	}
	
	
	public void changeDispatchStatus(String orderId) {
		Order order = orderService.changeDispatchStatus(orderId);
		producer.publishToStockUpdateTopic(order);
	}

} 

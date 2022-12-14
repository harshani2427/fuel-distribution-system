package com.fuel.allocationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fuel.allocationservice.controller.AllocationController;
import com.fuel.orderservice.model.Order;


@Service
public class Consumer {
	
	@Autowired
	AllocationController allocationController;
	
	@KafkaListener(topics= "allocation-topic", groupId = "allocation-group" )
	public void listenAllocationTopic(Order order){
		System.out.println("Listen to allocation topic in AllocationService from OrderService " + order);
		
		allocationController.orderAllocation(order);	
	}
	
	@KafkaListener(topics= "dispatch-stock-update-topic", groupId = "allocation-group" )
	public void listenDispatchStockUpdateTopic(Order order){
		System.out.println("Listen to dispatch-stock-update topic in AllocationService from OrderService " + order);
		
		allocationController.updateStock(order);	
	}

}

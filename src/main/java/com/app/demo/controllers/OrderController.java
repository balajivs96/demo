package com.app.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dto.OrderDto;
import com.app.demo.entity.AmUser;
import com.app.demo.serviceImpl.OrderServiceImpl;
import com.app.demo.utils.ApiResponse;

@RestController
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@PostMapping("")
	public ResponseEntity<?> addOrder(@RequestParam Long userId, @RequestBody OrderDto order) {
		ApiResponse<?> response = new ApiResponse<>(orderServiceImpl.placeOrder(userId, order), HttpStatus.OK.value(),
				null);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<AmUser> removeOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderServiceImpl.cancelOrder(userId, orderId));
    }
}

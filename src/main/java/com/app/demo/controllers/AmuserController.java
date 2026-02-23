package com.app.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.entity.AmUser;
import com.app.demo.serviceImpl.UserServiceImpl;
import com.app.demo.utils.ApiResponse;

@RestController
@RequestMapping("/users")
public class AmuserController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@GetMapping("")
	public ResponseEntity<ApiResponse<List<AmUser>>> getAllUsers() {
		List<AmUser> users = userServiceImpl.getAllUsers();
		ApiResponse<List<AmUser>> response = new ApiResponse<>(users, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> getUserById(@PathVariable Long id) {
		List<AmUser> user = userServiceImpl.getUserById(id);
		ApiResponse<List<AmUser>> response = new ApiResponse<>(user, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(response);
	}
}

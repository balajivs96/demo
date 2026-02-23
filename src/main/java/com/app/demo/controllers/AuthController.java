package com.app.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dto.LoginDto;
import com.app.demo.dto.UserDto;
import com.app.demo.serviceImpl.AuthServiceImpl;
import com.app.demo.serviceImpl.UserServiceImpl;
import com.app.demo.utils.ApiResponse;
import com.app.demo.utils.JwtUtil;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

	private UserServiceImpl userServiceImpl;
	private AuthServiceImpl authServiceImpl;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	private ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
		String data = userServiceImpl.createUser(userDto);
		ApiResponse<String> response = new ApiResponse<>(data, HttpStatus.CREATED.value(), null);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	private ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
		String data = authServiceImpl.loginUser(loginDto);
		ApiResponse<String> response = new ApiResponse<>(data, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(response);
	}
}

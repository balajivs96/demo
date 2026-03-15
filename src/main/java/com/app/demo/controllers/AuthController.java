package com.app.demo.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dto.LoginDto;
import com.app.demo.dto.RefreshTokenRequestDto;
import com.app.demo.dto.UserDto;
import com.app.demo.serviceImpl.AuthServiceImpl;
import com.app.demo.serviceImpl.UserServiceImpl;
import com.app.demo.utils.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

	private UserServiceImpl userServiceImpl;
	private AuthServiceImpl authServiceImpl;

	@PostMapping("/register")
	private ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
		String data = userServiceImpl.createUser(userDto);
		ApiResponse<String> response = new ApiResponse<>(data, HttpStatus.CREATED.value(), null);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	private ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto, HttpServletResponse response) {
		Map<String, String> data = authServiceImpl.loginUser(loginDto, response);
		ApiResponse<Map<String, String>> res = new ApiResponse<>(data, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String data = authServiceImpl.refreshToken(request, response); // service reads from cookies
		ApiResponse<?> res = new ApiResponse<>(data, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		response.addHeader("Set-Cookie", "access_token=; HttpOnly; Path=/; Max-Age=0; SameSite=Lax");
		response.addHeader("Set-Cookie", "refresh_token=; HttpOnly; Path=/; Max-Age=0; SameSite=Lax");
		ApiResponse<String> res = new ApiResponse<>("Logout successful", HttpStatus.OK.value(), null);
		return ResponseEntity.ok(res);
	}
}

package com.app.demo.serviceImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.demo.constants.AppConstants;
import com.app.demo.dto.LoginDto;
import com.app.demo.entity.AmUser;
import com.app.demo.handler.ApiLogicException;
import com.app.demo.repositories.AmUserRepository;
import com.app.demo.utils.JwtUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl {

	private final AmUserRepository amUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public String loginUser(LoginDto loginDto) {
		AmUser user = amUserRepository.findByEmail(loginDto.getEmail())
				.orElseThrow(() -> new ApiLogicException(404, AppConstants.NO_USERS_FOUND));
		if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
			throw new ApiLogicException(400, AppConstants.INVALID_CREDENTIALS);
		}
		String token = jwtUtil.generateToken(user.getEmail());

		return token;
	}
}
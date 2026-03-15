package com.app.demo.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.demo.constants.AppConstants;
import com.app.demo.dto.AuthResponseDto;
import com.app.demo.dto.LoginDto;
import com.app.demo.dto.RefreshTokenRequestDto;
import com.app.demo.entity.AmUser;
import com.app.demo.handler.ApiLogicException;
import com.app.demo.repositories.AmUserRepository;
import com.app.demo.utils.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl {

	private final AmUserRepository amUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	// loginUser
	public Map<String, String> loginUser(LoginDto loginDto, HttpServletResponse response) {
		AmUser user = amUserRepository.findByEmail(loginDto.getEmail())
				.orElseThrow(() -> new ApiLogicException(404, AppConstants.NO_USERS_FOUND));

		if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
			throw new ApiLogicException(400, AppConstants.INVALID_CREDENTIALS);
		}

		String token = jwtUtil.generateToken(user.getEmail());

		Cookie cookie = new Cookie("access_token", token);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // Set to true in production with HTTPS
		cookie.setPath("/");
		cookie.setMaxAge(1 * 60); // 5 mins
		// cookie.setMaxAge(24 * 60 * 60); // 1 day

		response.addCookie(cookie);
		Map<String, String> apiresponse = new HashMap<String, String>();
		apiresponse.put("message", "Login successful");
		apiresponse.put("token", token);

		return apiresponse;
	}

	// refreshToken
	public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			throw new ApiLogicException(401, "No cookies found");
		}

		String refreshToken = null;
		for (Cookie cookie : cookies) {
			if ("refresh_token".equals(cookie.getName())) {
				refreshToken = cookie.getValue();
				break;
			}
		}

		if (refreshToken == null || jwtUtil.isTokenExpired(refreshToken)) {
			throw new ApiLogicException(401, "Invalid or expired refresh token");
		}

		String email = jwtUtil.extractEmail(refreshToken);
		String newAccessToken = jwtUtil.generateToken(email);

		// Set new access token cookie
		Cookie cookie = new Cookie("access_token", newAccessToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // true in production
		cookie.setPath("/");
		cookie.setMaxAge(5 * 60); // 5 minutes
		response.addCookie(cookie);

		return "Token refreshed";
	}
}
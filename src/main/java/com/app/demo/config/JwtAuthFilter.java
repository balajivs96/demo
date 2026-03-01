package com.app.demo.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.app.demo.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Cookie cookie = WebUtils.getCookie(request, "access_token");

		if (cookie != null) {

			String token = cookie.getValue();

			try {
				String email = jwtUtil.extractEmail(token);

				if (email != null && !jwtUtil.isTokenExpired(token)
						&& SecurityContextHolder.getContext().getAuthentication() == null) {

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
							null, Collections.emptyList());

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}

			} catch (Exception e) {
				throw new RuntimeException("Invalid or expired JWT token");
			}
		}

		filterChain.doFilter(request, response);
	}

//	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
}

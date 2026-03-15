package com.app.demo.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

		String path = request.getServletPath();
		List<String> publicPaths = List.of("/auth/", "/h2", "/h2/", "/swagger-ui/", "/v3/api-docs/");

		boolean isPublic = publicPaths.stream().anyMatch(path::startsWith);
		if (isPublic) {
			filterChain.doFilter(request, response);
			return;
		}

		Cookie cookie = WebUtils.getCookie(request, "access_token");

		if (cookie != null) {
			String token = cookie.getValue();
			try {
				if (!jwtUtil.isTokenExpired(token)) {
					String email = jwtUtil.extractEmail(token);
					if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								email, null, Collections.emptyList());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			} catch (Exception e) {
				// Token invalid or corrupted → clear authentication but don’t throw
				// RuntimeException
				SecurityContextHolder.clearContext();
			}
		}

		// Proceed with filter chain even if token missing or invalid
		filterChain.doFilter(request, response);
	}

//	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
}

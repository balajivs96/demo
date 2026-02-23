package com.app.demo.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.app.demo.utils.ApiResponse;
import com.app.demo.utils.ErrorVM;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ApiResponse<Object> apiResponse = new ApiResponse<>();
		apiResponse.setData(null);
		apiResponse.setErrorVM(Collections
				.singletonList(new ErrorVM("Forbidden: You don't have permission to access this resource", 403)));
		apiResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

		String json = objectMapper.writeValueAsString(apiResponse);

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.getWriter().write(json);
		response.getWriter().flush();
	}
}
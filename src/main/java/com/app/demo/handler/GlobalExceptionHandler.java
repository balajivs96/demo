package com.app.demo.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.app.demo.constants.AppConstants;
import com.app.demo.utils.ApiResponse;
import com.app.demo.utils.ErrorVM;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ApiLogicException.class)
	public ResponseEntity<ApiResponse<String>> handleApiLogic(ApiLogicException ex) {
		log.error("ApiLogicException: {}", ex.getMessage());
		return buildResponse(ex.getMessage(), ex.getCode(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleNotFound(NoHandlerFoundException ex) {
		log.error("NoHandlerFoundException: {}", ex.getMessage());
		return buildResponse(AppConstants.NO_ENDPOINT_FOUND + ex.getHttpMethod() + " " + ex.getRequestURL(),
				HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ RuntimeException.class, Exception.class })
	public ResponseEntity<ApiResponse<String>> handleGeneric(Exception ex) {
		log.error("RuntimeException: {}", ex.getMessage());
		return buildResponse(AppConstants.INTERNAL_SERVER_ERROR, 1000, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ApiResponse<String>> buildResponse(String message, int code, HttpStatus status) {

		ApiResponse<String> response = new ApiResponse<>();
		List<ErrorVM> errors = new ArrayList<>();
		errors.add(new ErrorVM(message, code));

		response.setData(null);
		response.setErrorVM(errors);
		response.setStatus(status.value());

		return new ResponseEntity<>(response, status);
	}
}
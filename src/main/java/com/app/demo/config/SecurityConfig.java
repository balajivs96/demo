package com.app.demo.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.app.demo.utils.ApiResponse;
import com.app.demo.utils.ErrorVM;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthFilter jwtAuthFilter;

	public SecurityConfig(CustomAccessDeniedHandler accessDeniedHandler, JwtAuthFilter jwtAuthFilter) {
		this.accessDeniedHandler = accessDeniedHandler;
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.cors(cors -> {
				})
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/h2/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll().anyRequest()
						.authenticated())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
					ApiResponse<Object> apiResponse = new ApiResponse<>();
					apiResponse.setData(null);
					apiResponse.setErrorVM(Collections.singletonList(
							new ErrorVM("Unauthorized: Authentication is required to access this resource", 401)));
					apiResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

					String json = new ObjectMapper().writeValueAsString(apiResponse);
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.setContentType("application/json");
					response.getWriter().write(json);
					response.getWriter().flush();
				}).accessDeniedHandler(accessDeniedHandler));

		return http.build();
	}

	// global CORS configuration
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(List.of("http://localhost:4200"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
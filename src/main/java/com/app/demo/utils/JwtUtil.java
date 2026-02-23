package com.app.demo.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

	private final String SECRET = "your-256-bit-secret-demo-256-bit-demo";

	private SecretKey key;

	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String email) {
		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(key).compact();
	}

	public String extractUsername(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parse(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}
}

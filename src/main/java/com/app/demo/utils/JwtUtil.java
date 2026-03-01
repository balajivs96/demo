package com.app.demo.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.app.demo.handler.ApiLogicException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

	@Value("${app.jwt.secret}")
	private String secretKey;

	@Value("${app.jwt.expiry}")
	private long accessTokenExpiry;

	@Value("${app.jwt.refresh-expiry}")
	private long refreshTokenExpiry;

	// Get signing key
	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	// GENERATE TOKENS
	public String generateToken(String email) {
		return buildToken(email, accessTokenExpiry);
	}

	public String generateRefreshToken(String email) {
		return buildToken(email, refreshTokenExpiry);
	}

	private String buildToken(String email, long expiry) {
		return Jwts.builder().subject(email) // replaces setSubject()
				.issuedAt(new Date()) // replaces setIssuedAt()
				.expiration(new Date(System.currentTimeMillis() + expiry)) // replaces setExpiration()
				.signWith(getSigningKey()) // replaces signWith(algo, key)
				.compact();
	}

	// EXTRACT CLAIMS
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		return claimsResolver.apply(extractAllClaims(token));
	}

	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser().verifyWith(getSigningKey()) // replaces setSigningKey()
					.build() // need to call build()
					.parseSignedClaims(token) // replaces parseClaimsJws()
					.getPayload(); // replaces getBody()
		} catch (ExpiredJwtException e) {
			throw new ApiLogicException(401, "Token has expired");
		} catch (MalformedJwtException e) {
			throw new ApiLogicException(401, "Invalid token format");
		} catch (SecurityException e) {
			throw new ApiLogicException(401, "Invalid token signature");
		} catch (Exception e) {
			throw new ApiLogicException(401, "Token parsing failed");
		}
	}

	// VALIDATE TOKENS
	public boolean isTokenExpired(String token) {
		try {
			return extractExpiration(token).before(new Date());
		} catch (ApiLogicException e) {
			return true;
		}
	}

	public boolean validateToken(String token, String email) {
		String extractedEmail = extractEmail(token);
		return extractedEmail.equals(email) && !isTokenExpired(token);
	}
}
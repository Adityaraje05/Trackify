package com.asn.trac.api.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

	@Value("${app.jwt.secret}")
	private String secret;

	@Value("${app.jwt.expiration-ms}")
	private long expirationMs;

	private SecretKey getSigningKey() {
		// HS256 requires >= 256-bit key (>= 32 bytes)
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String username, String role) {
		return Jwts.builder()
			.setSubject(username)
			.claim("role", role)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public String extractUsername(String token) {
		return getAllClaims(token).getSubject();
	}

	public String extractRole(String token) {
		return getAllClaims(token).get("role", String.class);
	}

	public boolean isTokenValid(String token, String username) {
		final String subject = extractUsername(token);
		return subject.equals(username) && !isExpired(token);
	}

	private boolean isExpired(String token) {
		return getAllClaims(token).getExpiration().before(new Date());
	}

	private Claims getAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
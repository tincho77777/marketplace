package com.rest.marketplace.infrastructure.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.access-token-expiration}")
	private long accessTokenExpiration;

	@Value("${jwt.refresh-token-expiration}")
	private long refreshTokenExpiration;

	// genera un Access Token
	public String generateAccessToken(UserDetails userDetails) {
		var claims = new HashMap<String, Object>();
		claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority()); // agrega el rol al payload
		return buildToken(claims, userDetails.getUsername(), refreshTokenExpiration);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(new HashMap<>(), userDetails.getUsername(), refreshTokenExpiration);
	}

	private String buildToken(Map<String, Object> claims, String username, long refreshTokenExpiration) {
		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
				.signWith(getSigningKey())
				.compact();
	}

	// firma el token con la secret key
	private SecretKey getSigningKey() {
		byte[] keyBytes = Base64.getDecoder().decode(secretKey); // decodifica el Base64
		return Keys.hmacShaKeyFor(keyBytes); // crea la clave HMAC-SHA256
	}

	// valida si un token es válido para un usuario específico
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// extrae información del payload del token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject); // el subject es el email
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

}

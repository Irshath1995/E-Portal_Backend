package com.example.E_Portal.serviceImpl;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.E_Portal.dto.EmployeeDto;
import com.example.E_Portal.model.User;
import com.example.E_Portal.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	public String secretKey = "";
//	private final String secretKey = Base64.getEncoder().encodeToString("MySuperSecretKeyForJwtTesting12345".getBytes());

	public JwtServiceImpl() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String generarteToken(EmployeeDto employeeDto) {
		Map<String, Object> claim = new HashMap<>();
		claim.put("role", employeeDto.getDesignation());

		String token = Jwts.builder().claims().add(claim).subject(employeeDto.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)).and().signWith(getkey()).compact();

		System.out.println("Generated JWT: " + token);
		return token;
	}

	private SecretKey getkey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		System.out.println("secret key - " + secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String extractUserEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = exrtractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims exrtractAllClaims(String token) {
		return Jwts.parser().verifyWith((SecretKey) getkey()).build().parseSignedClaims(token).getPayload();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	@Override
	public boolean isTokenValid(String token, EmployeeDto employeeDto) {
		final String role = extractRole(token);
		System.out.println("role - " + role);
		final String userEmail = extractUserEmail(token);
		return userEmail.equals(employeeDto.getEmail()) && !isTokenExpired(token);
	}

	private String extractRole(String token) {
		return extractClaim(token, claims -> claims.get("role", String.class));
	}

	@Override
	public UsernamePasswordAuthenticationToken getAuthenticationToken(final String jwt,
			final Authentication existingAuth, EmployeeDto employeeDto) {
		Claims claims = exrtractAllClaims(jwt);

		final Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get("role").toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User userDetails = new User(employeeDto.getEmail(), "", authorities);

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}

}

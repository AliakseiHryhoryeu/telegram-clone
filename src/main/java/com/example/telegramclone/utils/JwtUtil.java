package com.example.telegramclone.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

// Sourcr code from
// https://github.com/viralpatel/java-create-validate-jwt-token/blob/master/src/main/java/net/viralpatel/jwt/JWTGenerateValidateHMAC.java

public class JwtUtil {
	static String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

	public static void main(String[] args) {

		String jwt = createJwtSignedHMAC();

		Jws<Claims> token = parseJwt(jwt);

		System.out.println(token.getPayload());
	}

	public static Jws<Claims> parseJwt(String jwtString) {

		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());

		Jws<Claims> jwt = Jwts.parser()
				.setSigningKey(hmacKey)
				.build()
				.parseClaimsJws(jwtString);

		return jwt;
	}

	public static String createJwtSignedHMAC() {

		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());

		Instant now = Instant.now();
		String jwtToken = Jwts.builder()
				.claim("name", "Jane Doe")
				.claim("email", "jane@example.com")
				.subject("jane")
				.id(UUID.randomUUID().toString())
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
				.signWith(hmacKey)
				.compact();

		return jwtToken;
	}

}
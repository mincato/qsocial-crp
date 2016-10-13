package com.qsocialnow.security;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.GsonBuilder;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.qsocialnow.security.exception.TokenExpiredException;
import com.qsocialnow.security.exception.TokenGenerationException;
import com.qsocialnow.security.exception.UnauthorizedException;

public class TokenHandler {

	private String headerContentType;
	private String simetricKey;
	private int expirationInMinutes;
	private String issuer;
	private String audience;
	private String subject;

	private static final String USER_KEY = "key";

	private static final long SECONDS_IN_A_MINUTE = 60;
	private static final long MILISECONDS_IN_A_SECOND = 1000;

	public UserData verifyToken(String token) {

		SignedJWT jwt = parseToken(token);

		verifySignature(jwt);

		JWTClaimsSet claims = getClaims(jwt);

		verifyIssuer(claims);
		verifyAudience(claims);
		verifySubject(claims);

		verifyExpiration(claims);

		return getUser(claims);
	}

	public UserData getExpiredUser(String token) {

		SignedJWT jwt = parseToken(token);

		verifySignature(jwt);

		JWTClaimsSet claims = getClaims(jwt);

		verifyIssuer(claims);
		verifyAudience(claims);
		verifySubject(claims);

		return getUser(claims);
	}

	public String createToken(UserData userData) {

		JWTClaimsSet jwtClaims = createJWTClaims(userData);

		// Create JWS header with HS256 algorithm
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).contentType(headerContentType)
				.customParam("exp", new Date().getTime()).build();

		SignedJWT jwt = new SignedJWT(header, jwtClaims);

		// sign the JSON Web Token
		try {
			JWSSigner signer = new MACSigner(simetricKey.getBytes());
			jwt.sign(signer);
		} catch (Exception e) {
			throw new TokenGenerationException(e);
		}

		return jwt.serialize();
	}

	private Date calculateExpirationTime() {
		return new Date(new Date().getTime() + MILISECONDS_IN_A_SECOND * SECONDS_IN_A_MINUTE * expirationInMinutes);
	}

	private JWTClaimsSet createJWTClaims(UserData userData) {

		String userJson = new GsonBuilder().serializeNulls().create().toJson(userData);
		
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(subject).expirationTime(calculateExpirationTime())
				.claim(USER_KEY, userJson).issuer(issuer).audience(audience)
				.jwtID(UUID.randomUUID().toString()).build();

		return claimsSet;
	}

	private SignedJWT parseToken(String token) {
		SignedJWT jwt = null;
		try {
			jwt = SignedJWT.parse(token);
		} catch (Exception e) {
			throw new UnauthorizedException(e);
		}
		return jwt;
	}

	private void verifySignature(SignedJWT jwt) {
		boolean verifiedSignature = false;
		try {
			JWSVerifier verifier = new MACVerifier(simetricKey);
			verifiedSignature = jwt.verify(verifier);
		} catch (Exception e) {
			throw new UnauthorizedException(e);
		}
		if (!verifiedSignature) {
			throw new UnauthorizedException("verifySignature FAILED!");
		}
	}

	private JWTClaimsSet getClaims(SignedJWT jwt) {
		JWTClaimsSet claims = null;
		try {
			claims = jwt.getJWTClaimsSet();
		} catch (Exception e) {
			throw new UnauthorizedException(e);
		}
		return claims;
	}

	private void verifyExpiration(JWTClaimsSet claims) {
		Date expiration = claims.getExpirationTime();
		Date now = new Date();
		if (now.getTime() >= expiration.getTime()) {
			throw new TokenExpiredException("verifyExpiration FAILED!");
		}
	}

	private void verifyIssuer(JWTClaimsSet claims) {
		if (!issuer.equals(claims.getIssuer())) {
			throw new UnauthorizedException("verifyIssuer FAILED!");
		}
	}

	private void verifyAudience(JWTClaimsSet claims) {
		List<String> au = claims.getAudience();
		if (au == null || au.size() != 1) {
			throw new UnauthorizedException("verifyAudience FAILED!");
		}
		if (!audience.equals(au.get(0))) {
			throw new UnauthorizedException("verifyAudience FAILED!");
		}
	}

	private void verifySubject(JWTClaimsSet claims) {
		if (!subject.equals(claims.getSubject())) {
			throw new UnauthorizedException("verifySubject FAILED!");
		}
	}

	private UserData getUser(JWTClaimsSet claims) {
		String userJson = (String) claims.getClaim(USER_KEY);
		if (StringUtils.isBlank(userJson)) {
			throw new UnauthorizedException("USER not found in custom claim's token");
		}
		try {
			return new GsonBuilder().create().fromJson(userJson, UserData.class);
		} catch (Exception e) {
			throw new UnauthorizedException(e);
		}
	}

	public String getHeaderContentType() {
		return headerContentType;
	}

	public void setHeaderContentType(String headerContentType) {
		this.headerContentType = headerContentType;
	}

	public String getSimetricKey() {
		return simetricKey;
	}

	public void setSimetricKey(String simetricKey) {
		this.simetricKey = simetricKey;
	}

	public int getExpirationInMinutes() {
		return expirationInMinutes;
	}

	public void setExpirationInMinutes(int expirationInMinutes) {
		this.expirationInMinutes = expirationInMinutes;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}

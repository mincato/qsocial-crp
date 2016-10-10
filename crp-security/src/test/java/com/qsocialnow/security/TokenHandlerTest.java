package com.qsocialnow.security;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenHandlerTest {
	
	private static final Logger LOGGER = Logger.getLogger(TokenHandlerTest.class);
	
	private TokenHandler tokenHandler;

	@Before
	public void init() {
		tokenHandler = new TokenHandler();
		tokenHandler.setHeaderContentType("text/plain");
		tokenHandler.setSimetricKey("eeeb0a3e-d96b-4229-9201-d9c0b1a394be");
		tokenHandler.setExpirationInMinutes(10);
		tokenHandler.setIssuer("QSocialNow");
		tokenHandler.setAudience("http://qsocialnow.com");
		tokenHandler.setSubject("QSocialNow User");
	}
	
	@Test
	public void generateToken() {
		UserData data = new UserData();
		data.setUsername("adrian");
		
		String token = tokenHandler.createToken(data);
		
		Assert.assertNotNull(token);
		LOGGER.info("Generated token = [" + token + "]");
		
		UserData data2 = tokenHandler.verifyToken(token);
		Assert.assertEquals(data2.getUsername(), data.getUsername());		
	}
	
	
}

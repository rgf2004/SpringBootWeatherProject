package com.weather.data.token;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private SecureRandom random = new SecureRandom();

	public String generateToken() {
		
		logger.info("GenerateToken Method has been called");
		return new BigInteger(130, random).toString(32);
	}

}

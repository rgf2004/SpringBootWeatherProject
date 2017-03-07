package com.weather.core.test;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weather.core.constants.Constants;
import com.weather.core.exception.WeatherException;
import com.weather.core.json.JsonHandler;

@Configuration
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJsonHandler {

	@Autowired
	JsonHandler jsonHandler;
	
	@Test
	public void testJsonHandlerSuccess()
	{
		String response = jsonHandler.createSuccessJsonResponse();
		JSONObject object = new JSONObject(response);
		Assert.assertTrue(object.getString(Constants.MESSAGE).equals(Constants.SUCCESS));
	}
	
	@Test
	public void testJsonHandlerExceptionCase()
	{
		String execptionMessage = "Test Exception";
		int exceptionCode = -1;
		WeatherException ex = new WeatherException(exceptionCode, execptionMessage);
		String response = jsonHandler.createJsonResponseFromException(ex);
		JSONObject object = new JSONObject(response);
		Assert.assertTrue(object.getString(Constants.MESSAGE).equals(execptionMessage));
	}
	
	
}

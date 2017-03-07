package com.weather.core.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weather.core.httpclient.HttpClient;
import com.weather.core.httpclient.beans.HttpGetRequest;

@Configuration
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestHttpClient {

	@Test
	public void testHttpClient() throws Exception {
		HttpGetRequest getRequest = new HttpGetRequest();
		getRequest.setURL("http://rest-service.guides.spring.io/greeting");

		HttpClient client = new HttpClient();

		String response = client.GetRequestJson(getRequest);
		Assert.assertNotNull(response);

	}

}

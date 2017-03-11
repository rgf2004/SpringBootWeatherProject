package com.weather.rest.test;

import javax.transaction.Transactional;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weather.rest.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@Transactional
public class TestUser {

	@Autowired
	User userRest;
	
	
	@Test
	@Rollback
	public void testCreateUser()
	{
		String userName = "";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "012345677";
		boolean admin = false;
						
		com.weather.data.model.User user = new com.weather.data.model.User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
		
		Response response = userRest.create(user);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("Name Field is Empty or too short minimum length is 8 characters"));
	}
	
	

	@Test
	@Rollback
	public void testAddUserWithInvalidMobile()
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567";
		boolean admin = false;
		
		com.weather.data.model.User user = new com.weather.data.model.User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);

		Response response = userRest.create(user);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("Mobile No should be Numbers only and 11 digits"));
		
	}
	
	@Test
	@Rollback
	public void testAddNewUser()
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = false;
		
		com.weather.data.model.User user = new com.weather.data.model.User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);

		Response response = userRest.create(user);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));	
							
	}
	
	
	@Test
	@Rollback
	public void testAddNewUserWithExistingEmail()
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = false;
		
		com.weather.data.model.User user = new com.weather.data.model.User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);

		userRest.create(user);
		
		com.weather.data.model.User user_2 = new com.weather.data.model.User();
		user_2.setName(userName+"anything");
		user_2.setPassword(password+"anything");				
		user_2.setEmail(email);
		user_2.setMobile(mobile);
		user_2.setAdmin(!admin);
		
		Response response = userRest.create(user);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("Email is already existing"));	
													
	}
	
	
	@Test
	@Rollback
	public void testNotAdminTryLoginAsAdmin() 
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = true;
		
		com.weather.data.model.User user = new com.weather.data.model.User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);

		Response response = userRest.create(user);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));
				
	}
		
	@Test
	@Rollback
	public void testUserLoginSuccess()
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = false;
		
		com.weather.data.model.User user = new com.weather.data.model.User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
		
		userRest.create(user);
		
		Response response = userRest.login(email, password);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));
	}
	
	
	@Test
	@Rollback
	public void testUserLoginFailed()
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = false;
		
		com.weather.data.model.User user = new com.weather.data.model.User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
		
		userRest.create(user);
		
		Response response = userRest.login(email, password+"anything");
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("Email or Passowrd is not correct"));
	}
	
	@Test
	@Rollback
	public void testUserLogout()
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = false;
		
		com.weather.data.model.User user = new com.weather.data.model.User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
		
		userRest.create(user);
		
		Response responseLogin = userRest.login(email, password);
		
		NewCookie cookie = responseLogin.getCookies().get("token");
		
		Response response = userRest.logout(cookie);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));
				
	}
	
	@Test
	@Rollback
	public void testGetWeather()
	{		
		
		Response response = userRest.getWeatherDetails();
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));
		
		Assert.assertNotNull(responseObj.get("weather"));
				
	}
	
}

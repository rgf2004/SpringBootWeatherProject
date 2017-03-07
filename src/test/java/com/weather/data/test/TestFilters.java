package com.weather.data.test;

import javax.transaction.Transactional;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.jersey.server.ContainerRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weather.core.constants.Constants;
import com.weather.core.exception.WeatherException;
import com.weather.data.dao.UserDao;
import com.weather.data.filter.AuthenticateAsAdminFilter;
import com.weather.data.filter.AuthenticateAsCustomerFilter;
import com.weather.data.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@Transactional
public class TestFilters {

	@Autowired
	AuthenticateAsAdminFilter authenticateAsAdminFilter;
	
	@Autowired
	AuthenticateAsCustomerFilter authenticateAsCustomerFilter;
	
	@Autowired
	UserDao userDao;
	
	@Test
	public void testCustomerFilterWithValidToken() throws WeatherException, IOException
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = false;
		
		User user = new User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
				
		userDao.createUser(user);
		
		String token = userDao.login(email, password, admin);
		
		Map<String, Cookie> cookies = new HashMap<>();
		cookies.put(Constants.COOKIE_TOKEN_NAME,new Cookie(Constants.COOKIE_TOKEN_NAME, token));
		
		ContainerRequestContext requestCtxt = mock(ContainerRequestContext.class);
		when(requestCtxt.getCookies()).thenReturn(cookies);
		
		authenticateAsCustomerFilter.filter(requestCtxt);
	}

	@Test
	public void testCustomerFilterWithInvalidToken() throws WeatherException
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = false;
		
		User user = new User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
				
		userDao.createUser(user);
		
		String token = userDao.login(email, password, admin);
		
		Map<String, Cookie> cookies = new HashMap<>();
		cookies.put(Constants.COOKIE_TOKEN_NAME,new Cookie(Constants.COOKIE_TOKEN_NAME, token+"anything"));
		
		ContainerRequestContext requestCtxt = mock(ContainerRequestContext.class);
		when(requestCtxt.getCookies()).thenReturn(cookies);
		
		try {
			authenticateAsCustomerFilter.filter(requestCtxt);
			// throw new AssertionError("something wrong exception should be fired here") // There should be check here if abortWith() method has been called
		} catch (Exception e) {

		}
	}
	
	@Test
	public void testAdminFilterWithValidToken() throws WeatherException, IOException
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = true;
		
		User user = new User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
				
		userDao.createUser(user);
		
		String token = userDao.login(email, password, admin);
		
		Map<String, Cookie> cookies = new HashMap<>();
		cookies.put(Constants.COOKIE_TOKEN_NAME,new Cookie(Constants.COOKIE_TOKEN_NAME, token));
		
		ContainerRequestContext requestCtxt = mock(ContainerRequestContext.class);
		when(requestCtxt.getCookies()).thenReturn(cookies);
		
		authenticateAsAdminFilter.filter(requestCtxt);
	}

	@Test
	public void testAdminFilterWithInvalidToken() throws WeatherException
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = true;
		
		User user = new User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
				
		userDao.createUser(user);
		
		String token = userDao.login(email, password, admin);
		
		Map<String, Cookie> cookies = new HashMap<>();
		cookies.put(Constants.COOKIE_TOKEN_NAME,new Cookie(Constants.COOKIE_TOKEN_NAME, token+"anything"));
		
		ContainerRequestContext requestCtxt = mock(ContainerRequestContext.class);
		when(requestCtxt.getCookies()).thenReturn(cookies);
		
		try {
			authenticateAsAdminFilter.filter(requestCtxt);
			// throw new AssertionError("something wrong exception should be fired here") // There should be check here if abortWith() method has been called
		} catch (IOException e) {
	
		}
	}	
	
	
	@Test
	public void testAdminFilterWithCustomerdToken() throws WeatherException
	{
		String userName = "user user";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "01234567890";
		boolean admin = false;
		
		User user = new User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
				
		userDao.createUser(user);
		
		String token = userDao.login(email, password, admin);
		
		Map<String, Cookie> cookies = new HashMap<>();
		cookies.put(Constants.COOKIE_TOKEN_NAME,new Cookie(Constants.COOKIE_TOKEN_NAME, token));
		
		ContainerRequest requestCtxt = mock(ContainerRequest.class);
		when(requestCtxt.getCookies()).thenReturn(cookies);
		
		try {
			authenticateAsAdminFilter.filter(requestCtxt);
			// throw new AssertionError("something wrong exception should be fired here") // There should be check here if abortWith() method has been called
		} catch (IOException e) {
	
		}
	}	
}

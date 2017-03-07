package com.weather.data.test;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weather.core.exception.WeatherException;
import com.weather.data.dao.UserDao;
import com.weather.data.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@Transactional
public class TestUserDao {

	@Autowired
	UserDao userDao;
	

	@Test
	@Rollback
	public void testAddUserWithInvalidName()
	{
		String userName = "";
		String password = "12345678";
		String email = "user_user@user.com";
		String mobile = "012345677";
		boolean admin = false;
		
		User user = new User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
		
		try {
			userDao.createUser(user);			
		} catch (WeatherException e) {
			Assert.assertTrue(e.getMessage().equals("Name Field is Empty or too short minimum length is 8 characters"));
		}
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
		
		User user = new User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
		
		try {
			userDao.createUser(user);			
		} catch (WeatherException e) {
			Assert.assertTrue(e.getMessage().equals("Mobile No should be Numbers only and 11 digits"));
		}
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
		
		User user = new User();
		user.setName(userName);
		user.setPassword(password);				
		user.setEmail(email);
		user.setMobile(mobile);
		user.setAdmin(admin);
		
		try {
			userDao.createUser(user);
		} catch (WeatherException e) {
			throw new AssertionError("User hasn't beeen created although it should be");
		}
						
	}
	
	
	@Test
	@Rollback
	public void testAddNewUserWithExistingEmail() throws WeatherException
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
			
		User user_2 = new User();
		user_2.setName(userName+"anything");
		user_2.setPassword(password+"anything");				
		user_2.setEmail(email);
		user_2.setMobile(mobile);
		user_2.setAdmin(!admin);
		
		try
		{
			userDao.createUser(user_2);
		}
		catch (WeatherException ex)
		{
			Assert.assertTrue(ex.getMessage().equals("Email is already existing"));
		}											
	}
	
	
	@Test
	@Rollback
	public void testNotAdminTryLoginAsAdmin() throws WeatherException
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
		
		String token = userDao.login(email, password, true);
		
		Assert.assertNotNull(token);
		
	}
	
	@Test
	@Rollback
	public void testLoginWithUserNotAdmin() throws WeatherException
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
		
		Assert.assertNotNull(token);
		
	}
	
	@Test
	@Rollback
	public void testUserAccess() throws WeatherException
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
					
		boolean valid = userDao.isValidUserToken(token, admin);
		
		Assert.assertTrue(valid == true);
	}
	
	
	@Test
	@Rollback
	public void testAdminTryToAccessAsAdmin() throws WeatherException
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
					
		boolean valid = userDao.isValidUserToken(token, admin);
		
		Assert.assertTrue(valid == true);
	}
	
	@Test
	@Rollback
	public void testUserTryToAccessAsAdmin() throws WeatherException
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
					
		boolean valid = userDao.isValidUserToken(token, !admin);
		
		Assert.assertTrue(valid == false);
	}
	
	@Test
	@Rollback
	public void testLogout() throws WeatherException
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
		
		userDao.logout(token);
		
		boolean valid = userDao.isValidUserToken(token, admin);
		
		Assert.assertTrue(valid == false);
	}
	
}

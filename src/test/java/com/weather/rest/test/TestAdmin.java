package com.weather.rest.test;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weather.data.model.Note;
import com.weather.data.model.PredefinedNote;
import com.weather.rest.Admin;
import com.weather.rest.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@Transactional
public class TestAdmin {

	@Autowired
	User userRest;
	
	@Autowired
	Admin adminRest;
	
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
		
		Response response = adminRest.login(email, password);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("User doesn't have admin privilage"));
	}
	
	
	@Test
	@Rollback
	public void testUserLoginSuccess()
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
		
		userRest.create(user);
		
		Response response = adminRest.login(email, password);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));
	}
	
	@Test
	@Rollback
	public void testAddNewNoteWithShortBody()
	{
		String noteBody = "short";
		
		Note note = new Note();
		note.setNoteBody(noteBody);
		
		Response response = adminRest.createNote(note);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("Note Field is Empty or too short minimum length is 10 characters"));	
	}
	
	@Test
	@Rollback
	public void testAddNewNote() 
	{
		String noteBody = "This is a test note";
		
		Note note = new Note();
		note.setNoteBody(noteBody);
		
		Response response = adminRest.createNote(note);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));	
	}
	
	@Test
	@Rollback
	public void testAddNewPredefinedNoteWithShortNote() 
	{
		String noteBody = "short";
		int range = 1;
		
		PredefinedNote note = new PredefinedNote();		
		note.setNoteBody(noteBody);
		
		
		Response response = adminRest.createPredefinedNote(range, note);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("Note Field is Empty or too short minimum length is 10 characters"));	

	}
	
	@Test
	@Rollback
	public void testAddNewPredefinedNote() 
	{
		String noteBody = "This is a test predifned note";
		int range = 1;
		
		PredefinedNote note = new PredefinedNote();		
		note.setNoteBody(noteBody);
		
		
		Response response = adminRest.createPredefinedNote(range, note);
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));	

	}
	
	@Test
	@Rollback
	public void testGetNoteseNote() 
	{
		String noteBody = "This is a test note";
		
		Note note = new Note();
		note.setNoteBody(noteBody);
		
		adminRest.createNote(note);
		
		adminRest.getAllNotes();
		
		Response response = adminRest.getAllNotes();
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));
				
	}

	@Test
	@Rollback
	public void testGetPredefinedNote() 
	{
		
		adminRest.getAllPredefinedNotes();
		
		Response response = adminRest.getAllNotes();
		
		JSONObject responseObj = new JSONObject((String)response.getEntity());
		
		Assert.assertTrue(responseObj.getString("message").equals("SUCCESS"));
				
	}
	
}


package com.weather.data.test;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weather.core.exception.WeatherException;
import com.weather.data.dao.NoteDao;
import com.weather.data.enums.PreDefinedNoteRangeEnum;
import com.weather.data.model.Note;
import com.weather.data.model.PredefinedNote;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@Transactional
public class TestNoteDao {

	@Autowired
	NoteDao noteDao;
	
	@Test
	@Rollback
	public void testAddNewNoteWithShortBody()
	{
		String noteBody = "short";
		
		Note note = new Note();
		note.setNoteBody(noteBody);
		
		try {
			noteDao.createNote(note);
		} catch (WeatherException e) {
			Assert.assertTrue(e.getMessage().equals("Note Field is Empty or too short minimum length is 10 characters"));
		}
	
	}
	
	@Test
	@Rollback
	public void testAddNewNote() throws WeatherException
	{
		String noteBody = "This is a test note";
		
		Note note = new Note();
		note.setNoteBody(noteBody);
		
		noteDao.createNote(note);
		
		List<Note> note_2 = noteDao.getAllNotes();
		Assert.assertTrue(note_2.size() > 0);	
	}
	
	@Test
	@Rollback
	public void testAddNewPredefinedNoteWithShortNote() throws WeatherException
	{
		String noteBody = "short";
		int fromDegree = 1;
		int toDegree = 10;
		PreDefinedNoteRangeEnum range = PreDefinedNoteRangeEnum.PREDIFNED_NOTE_RANGE_3;
		
		PredefinedNote note = new PredefinedNote();		
		note.setNoteBody(noteBody);
		note.setFromDegree(fromDegree);
		note.setToDegree(toDegree);
		note.setNoteRange(range);
		
		try {
			noteDao.createPredefinedNote(2, note);
		} catch (WeatherException e) {
			Assert.assertTrue(e.getMessage().equals("Note Field is Empty or too short minimum length is 10 characters"));
		}
	
	}
	
	@Test
	@Rollback
	public void testAddNewPredefinedNote() throws WeatherException
	{
		String noteBody = "This is a test predifned note";
		PreDefinedNoteRangeEnum range = PreDefinedNoteRangeEnum.PREDIFNED_NOTE_RANGE_2;
		
		PredefinedNote note = new PredefinedNote();		
		note.setNoteBody(noteBody);
		note.setNoteRange(range);
		
		noteDao.createPredefinedNote(2,note);
	
		List<PredefinedNote> note_2 = noteDao.getAllPredefinedNotes();
		
		Assert.assertTrue(note_2.size() > 0);
		
	}
	
	@Test
	@Rollback
	public void testGetFutureNote() throws WeatherException
	{
		String noteBody = "This is a test note";
		
		Note note = new Note();
		note.setNoteBody(noteBody);
		
		noteDao.createNote(note);
		
		String note_2 = noteDao.getTodayNote(0);
		
		Assert.assertTrue(noteBody.equals(note_2));
		
	}
	
}


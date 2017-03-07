package com.weather.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.weather.core.exception.WeatherException;
import com.weather.data.enums.PreDefinedNoteRangeEnum;
import com.weather.data.model.Note;
import com.weather.data.model.PredefinedNote;
import com.weather.data.validation.Validator;

@Repository
public class NoteDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager em;

	@Autowired
	@Qualifier("noteValidator")
	Validator noteValidator;

	@Transactional
	public void createNote(Note note) throws WeatherException {
		try {

			logger.info("Create Note Method has been called");
			logger.debug("Parameters : Note [{}]", note.toString());

			noteValidator.validate(note);

			this.em.persist(note);

		} catch (WeatherException wex) {

			logger.error("Validation Exception while Validating new note", wex);
			throw wex;

		} catch (Exception ex) {

			logger.error("Exception Occured while creating new note", ex);
			throw new WeatherException(-1, "Technical Error has been occured please check log file", ex);
		}
	}

	@Transactional
	public void createPredefinedNote(int rangeID, PredefinedNote note) throws WeatherException {
		try {

			logger.info("Create Predefine Note Method has been called");
			logger.debug("Parameters : Note [{}]", note.toString());

			if (rangeID < 0 || rangeID > 3)
				throw new WeatherException(-10, "Note Predefined Range is not valid supported ranges {0,1,2,3}");
			
			note.setNoteRange(PreDefinedNoteRangeEnum.fromInt(rangeID));
			
			noteValidator.validate(note);

			this.em.persist(note);

		} catch (WeatherException wex) {

			logger.error("Validation Exception while Validating new note", wex);
			throw wex;

		} catch (Exception ex) {

			logger.error("Exception Occured while creating new note", ex);
			throw new WeatherException(-1, "Technical Error has been occured please check log file", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Note> getAllNotes() throws WeatherException {
		
		Query query = em.createQuery("from " + Note.class.getSimpleName() + " n order by n.id desc");
		
		List<Note> notes = query.getResultList();
		
		return notes;
	}
	
	@SuppressWarnings("unchecked")
	public List<PredefinedNote> getAllPredefinedNotes() throws WeatherException {
		
		Query query = em.createQuery("from " + PredefinedNote.class.getSimpleName() + " n order by n.id desc");
		
		List<PredefinedNote> notes = query.getResultList();
		
		return notes;
	}
	
	@SuppressWarnings("unchecked")
	public String getTodayNote(double degree)
	{
		Query noteQuery = em.createQuery("from " + Note.class.getSimpleName() + " n where n.noteDate = curdate() order by n.id desc");
	
		List<Note> notes = noteQuery.getResultList();
		if (notes != null & notes.size() > 0 )
			return notes.get(0).getNoteBody();
		
		Query predefinedNoteQuery = em.createQuery("from " + PredefinedNote.class.getSimpleName() + " n where "+ (int)Math.round(degree) +" between n.fromDegree and n.toDegree order by n.id desc");
		List<PredefinedNote> predefinedNotes = predefinedNoteQuery.getResultList();
		if (predefinedNotes != null & predefinedNotes.size() > 0 )
			return predefinedNotes.get(0).getNoteBody();
		
		return null;
		
	}

}

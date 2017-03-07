package com.weather.data.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weather.core.exception.WeatherException;
import com.weather.data.model.Note;
import com.weather.data.model.PredefinedNote;

@Component(value = "noteValidator")
public class NoteValidator implements Validator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ValidationUtil validationUtil;

	@Override
	public void validate(Object obj) throws WeatherException {

		logger.info("Validating Note Method has been called");
		logger.debug("Parameters : obj [{}]", obj.toString());

		if (obj instanceof Note) {
			Note note = null;
			note = (Note) obj;
			if (!validationUtil.checkString(note.getNoteBody(), 10)) {
				throw new WeatherException(-10, "Note Field is Empty or too short minimum length is 10 characters");
			}
		} else if (obj instanceof PredefinedNote) {
			PredefinedNote note = null;
			note = (PredefinedNote) obj;
			if (!validationUtil.checkString(note.getNoteBody(), 10)) {
				throw new WeatherException(-10, "Note Field is Empty or too short minimum length is 10 characters");
			}
		} else
			throw new WeatherException(-50, "Passed object is not of type Note");

	}

}

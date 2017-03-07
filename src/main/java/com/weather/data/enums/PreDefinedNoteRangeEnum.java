package com.weather.data.enums;

import com.weather.core.exception.WeatherException;

public enum PreDefinedNoteRangeEnum {


	PREDIFNED_NOTE_RANGE_0, 
	PREDIFNED_NOTE_RANGE_1, 
	PREDIFNED_NOTE_RANGE_2, 
	PREDIFNED_NOTE_RANGE_3;

	public static PreDefinedNoteRangeEnum fromInt(int range) throws WeatherException
	{
		switch (range)
		{
		case 0:
			return PreDefinedNoteRangeEnum.PREDIFNED_NOTE_RANGE_0;
		case 1:
			return PreDefinedNoteRangeEnum.PREDIFNED_NOTE_RANGE_1;
		case 2:
			return PreDefinedNoteRangeEnum.PREDIFNED_NOTE_RANGE_2;
		case 3:
			return PreDefinedNoteRangeEnum.PREDIFNED_NOTE_RANGE_3;
		default:
			throw new WeatherException(-10,"Invalid Range [" + range + "]");
		}
	}
	
}



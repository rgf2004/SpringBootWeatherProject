package com.weather.data.validation;

import org.springframework.stereotype.Service;

import com.weather.core.exception.WeatherException;

@Service
public interface Validator {

	public void validate(Object obj) throws WeatherException;
	
}

package com.weather.core.json;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.weather.core.constants.Constants;
import com.weather.core.exception.WeatherException;

@Service
@Component
public class JsonHandler {
		
	public JSONObject createJsonResponse(int status, String message) {
		JSONObject response = new JSONObject();

		response.put(Constants.STATUS, status);
		response.put(Constants.MESSAGE, message);

		return response;
	}
	
	public String createSuccessJsonResponse()
	{
		return createJsonResponse(0, Constants.SUCCESS).toString();
	}
	
	public String createJsonResponseFromException(WeatherException ex)
	{
		return createJsonResponse(ex.getCode(),ex.getMessage()).toString();
	}
	
	public String createSuccessJsonResponseWithBody(String bodyName,Object body)
	{
		JSONObject response = createJsonResponse(0, Constants.SUCCESS);
		response.put(bodyName, body);
		return response.toString();		
	}
	
}

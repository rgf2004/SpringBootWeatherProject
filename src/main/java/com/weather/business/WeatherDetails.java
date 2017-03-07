package com.weather.business;

import java.text.DecimalFormat;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.core.constants.Constants;
import com.weather.core.exception.WeatherException;
import com.weather.core.httpclient.HttpClient;
import com.weather.core.httpclient.beans.HttpGetRequest;
import com.weather.data.dao.NoteDao;

@Service
public class WeatherDetails {

	private final static String webHost = "http://api.openweathermap.org/";
	private final static String resource = "data/2.5/weather";
	private final static String APPID = "cd5caedec2ddab740bfeefb490b49931";

	// "http://api.openweathermap.org/data/2.5/weather?q=Cairo,eg&APPID=cd5caedec2ddab740bfeefb490b49931";

	@Autowired
	private NoteDao noteDao;

	public JSONObject getWeatherDetails() throws WeatherException {
		try {

			JSONObject weatherDetails = getCurruentTemp();
			
			String weatherNote = noteDao.getTodayNote(weatherDetails.getDouble("temp")); 
			
			weatherDetails.put(Constants.NOTE, weatherNote);
			
			return weatherDetails;

		} catch (Exception ex) {
			throw new WeatherException(-60, "Error Occured While Contacting Weather Server, Please check the log file",
					ex);
		}

	}

	private JSONObject getCurruentTemp() throws WeatherException {

		JSONObject serverResponse = getWeatherServerData();
		JSONObject mainElement = (JSONObject) serverResponse.get("main");
		return convertKelvinToCelsius(mainElement);

	}

	private JSONObject convertKelvinToCelsius(JSONObject in) {
		JSONObject out = new JSONObject();

		out.put("humidity", in.get("humidity"));
		out.put("pressure", in.get("pressure"));
		out.put("temp", convertKelvinToCelsius(in.getDouble("temp")));
		out.put("temp_min", convertKelvinToCelsius(in.getDouble("temp_min")));
		out.put("temp_max", convertKelvinToCelsius(in.getDouble("temp_max")));

		return out;
	}

	private double convertKelvinToCelsius(double temp) {

		DecimalFormat df = new DecimalFormat("#.##");      
		temp = Double.valueOf(df.format(temp - 274.15));
		return temp;
		

	}

	private JSONObject getWeatherServerData() throws WeatherException {

		try {

			JSONObject jsonRespone = null;
			HttpClient client = new HttpClient();
			HttpGetRequest getRequest = new HttpGetRequest();
			getRequest.setURL(generateURL());
			String response = client.GetRequestJson(getRequest);
			jsonRespone = new JSONObject(response);
			return jsonRespone;

		} catch (Exception ex) {
			throw new WeatherException(-60, "Error Occured While Contacting Weather Server, Please check the log file",
					ex);
		}

	}

	private String generateURL() {
		StringBuilder str = new StringBuilder();

		str.append(webHost);
		str.append(resource);
		str.append("?q=Cairo,eg");
		str.append("&APPID=");
		str.append(APPID);

		return str.toString();
	}

}

package com.weather.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.weather.business.WeatherDetails;
import com.weather.core.constants.Constants;
import com.weather.core.exception.WeatherException;
import com.weather.core.json.JsonHandler;
import com.weather.data.dao.UserDao;
import com.weather.data.filter.AuthenticateAsCustomer;

@Path("/user")
@Component
@Service
public class User {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserDao userDao;
	
	@Autowired
	WeatherDetails weatherDetails;

	@Autowired
	JsonHandler jsonHandler;

	/***
	 * Create User Method is the entry point for creating new customer.
	 * @param user object 
	 * @return JSON object represents the success/failure of the transaction
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(com.weather.data.model.User user) {

		logger.info("Create User Method has been called");
		logger.debug("Parameters : User [{}]", user);

		try {
			userDao.createUser(user);
			logger.info("User Creation done successfully");
			return Response.status(200).entity(jsonHandler.createSuccessJsonResponse()).build();
		} catch (WeatherException ex) {
			logger.error("Exception Occured while creating new user with error message [{}]", ex.getMessage());
			return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();
		}
	}

	/***
	 * Login User Method is the entry point for customer login.
	 * @param email
	 * @param password
	 * @return JSON object represents the success/failure of the transaction in addition to token cookie
	 */
	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam(Constants.EMAIL) String email, @QueryParam(Constants.PASSWORD) String password) {

		logger.info("login User Method has been called");
		logger.debug("Parameters : email [{}], password [*****]", email);

		try {
			String token = userDao.login(email, password,false);

			NewCookie tokenCookie = new NewCookie(Constants.COOKIE_TOKEN_NAME, token);

			logger.info("User logged in successfully");

			return Response.status(200).cookie(tokenCookie).entity(jsonHandler.createSuccessJsonResponse()).build();

		} catch (WeatherException ex) {
			logger.error("Exception Occured while user trying to login  with error message [{}]", ex.getMessage());
			return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();
		}
	}

	/***
	 * Logout User Method is the entry point for customer logout.
	 * @param token cookie
	 * @return JSON object represents the success/failure of the transaction
	 */
	@GET
	@AuthenticateAsCustomer
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@CookieParam(Constants.COOKIE_TOKEN_NAME) Cookie cookie) {

		logger.info("logout User Method has been called");
		logger.debug("Parameters : cookie [{}]", cookie);

		try {
			
			userDao.logout(cookie.getValue());

			logger.info("User logged out successfully");

			return Response.status(200).entity(jsonHandler.createSuccessJsonResponse()).build();

		} catch (WeatherException ex) {
			logger.error("Exception Occured while user trying to logout with error message [{}]", ex.getMessage());
			return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();
		}
	}
	
	
	/***
	 * GetWeatherDetails method to get the weather details.
	 * @return JSON object represents the success/failure of the transaction in addition to weather details
	 */
	@GET
	@AuthenticateAsCustomer
	@Path("/get-weather-details")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeatherDetails() {

		logger.info("Get Weather Details Method has been called");

		try {
			
			JSONObject weatherDetailsObj = weatherDetails.getWeatherDetails();
			
			logger.info("Weather Details : {}", weatherDetailsObj);

			return Response.status(200).entity(jsonHandler.createSuccessJsonResponseWithBody(Constants.WEATHER_ELEMENT, weatherDetailsObj)).build();

		} catch (WeatherException ex) {
			logger.error("Exception Occured while user trying to get weather details with error message [{}]", ex.getMessage());
			return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();
		}
	}
	
}

package com.weather.data.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weather.core.constants.Constants;
import com.weather.core.exception.WeatherException;
import com.weather.core.json.JsonHandler;
import com.weather.data.dao.UserDao;

@Provider
@AuthenticateAsAdmin
@Component
public class AuthenticateAsAdminFilter implements ContainerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserDao userDao;

	@Autowired
	JsonHandler jsonHandler;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		logger.info("AuthenticateAsCustomerFilter Started");

		Cookie tokenCookie = requestContext.getCookies().get(Constants.COOKIE_TOKEN_NAME);

		if (tokenCookie == null) {

			Response response = Response.status(200).entity(
					jsonHandler.createJsonResponseFromException(new WeatherException(-30, "token is not valid")))
					.build();
			requestContext.abortWith(response);

			logger.info("AuthenticateAsCustomerFilter Finished - token is not exist in request");

		} else {

			String token = tokenCookie.getValue();

			if (StringUtils.isEmpty(token) || !userDao.isValidUserToken(token, true)) {

				Response response = Response.status(200)
						.entity(jsonHandler
								.createJsonResponseFromException(new WeatherException(-30, "token is not valid")))
						.build();
				requestContext.abortWith(response);

				logger.info("AuthenticateAsCustomerFilter Finished - token is invalid");

			}
		}

		logger.info("AuthenticateAsCustomerFilter Finished - token is valid");

	}

}

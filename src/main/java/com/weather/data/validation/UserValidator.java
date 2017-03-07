package com.weather.data.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weather.core.exception.WeatherException;
import com.weather.data.model.User;

@Component(value="userValidator")
public class UserValidator implements Validator{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final String MOBILE_NO_PATTERN = "\\d{11}";

	@Autowired
	private ValidationUtil validationUtil;
	
	@Override
	public void validate(Object obj) throws WeatherException {
		
		User user = null;
		
		logger.info("Validating User Method has been called");
		logger.debug("Parameters : obj [{}]", obj.toString());

		
		if (obj instanceof User)
			user = (User)obj;
		else
			throw new WeatherException(-50, "Passed object is not of type User");
		
		if (!validationUtil.checkString(user.getName(), 8)) {
			throw new WeatherException(-10, "Name Field is Empty or too short minimum length is 8 characters");
		}

		if (!validationUtil.checkString(user.getPassword(), 8)) {
			throw new WeatherException(-10, "Password Field is Empty or too short minimum length is 8 characters");
		}

		if (!checkEmail(user.getEmail())) {
			throw new WeatherException(-10, "Email Field is invalid");
		}

		if (!checkMobileNo(user.getMobile())) {
			throw new WeatherException(-10, "Mobile No should be Numbers only and 11 digits");
		}
		
	}
		
	private boolean checkEmail(String email) {

		if (!validationUtil.checkString(email, 8))
			return false;

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean checkMobileNo(String mobileNo) {

		Pattern pattern = Pattern.compile(MOBILE_NO_PATTERN);
		Matcher matcher = pattern.matcher(mobileNo);
		return matcher.matches();
	}

}

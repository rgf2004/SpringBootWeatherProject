package com.weather.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.weather.core.exception.WeatherException;
import com.weather.data.model.User;
import com.weather.data.token.TokenGenerator;
import com.weather.data.validation.Validator;

@Repository
public class UserDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager em;

	@Autowired
	@Qualifier("userValidator")
	private Validator userValidator;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Transactional
	public void createUser(User user) throws WeatherException {
		try {

			logger.info("Create User Method has been called");
			logger.debug("Parameters : User [{}]", user.toString());

			userValidator.validate(user);

			if (checkEmailExisting(user.getEmail())) {
				throw new WeatherException(-10, "Email is already existing");
			}

			user.setPassword(DigestUtils.md2Hex(user.getPassword()));
			this.em.persist(user);

		} catch (WeatherException wex) {

			logger.error("Validation Exception while Validating new user", wex);
			throw wex;

		} catch (Exception ex) {

			logger.error("Exception Occured while creating new user", ex);
			throw new WeatherException(-1, "Technical Error has been occured please check log file", ex);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean checkEmailExisting(String userEmail) {

		Query query = em.createQuery("from " + User.class.getSimpleName() + " u where u.email = '" + userEmail + "'");

		List<User> users = query.getResultList();
		if (users != null && users.size() > 0)
			return true;
		else
			return false;

	}

	@Transactional
	public String login(String email, String password, boolean isAdmin) throws WeatherException {

		logger.info("login User Method has been called");
		logger.debug("Parameters : email [{}], password [{}]", email, password);

		User user = getUserByUserEmailAndPassword(email, password);

		if (user != null) {

			if (isAdmin) {
				if (!user.isAdmin())
					throw new WeatherException(-40, "User doesn't have admin privilage");
			}

			try {

				String token = tokenGenerator.generateToken();

				updateUserToken(user, token);

				return token;

			} catch (Exception ex) {

				logger.error("Exception Occured while user login", ex);
				throw new WeatherException(-1, "Technical Error has been occured please check log file", ex);

			}
		} else {

			throw new WeatherException(-20, "Email or Passowrd is not correct");

		}
	}

	@Transactional
	public void logout(String token) throws WeatherException {
		logger.info("logout User Method has been called");
		logger.debug("Parameters : token [{}]", token);

		User user = getUserBytoken(token);

		if (user != null) {

			try {

				updateUserToken(user, "");

			} catch (Exception ex) {

				logger.error("Exception Occured while user login", ex);
				throw new WeatherException(-1, "Technical Error has been occured please check log file", ex);

			}
		} else {

			throw new WeatherException(-20, "Passed Token is not valid");

		}

	}

	@SuppressWarnings("unchecked")
	private User getUserByUserEmailAndPassword(String userEmail, String password) {

		Query query = em.createQuery("from " + User.class.getSimpleName() + " u where u.email = '" + userEmail
				+ "' and u.password = '" + DigestUtils.md2Hex(password) + "'");

		List<User> users = query.getResultList();
		if (users != null && users.size() > 0)
			return users.get(0);
		else
			return null;
	}

	public boolean isValidUserToken(String token, boolean isAdmin) {

		User user = getUserBytoken(token);
		if (user == null)
			return false;
		else {
			if (isAdmin) {
				if (!user.isAdmin())
					return false;
			}

			return true;
		}

	}

	@SuppressWarnings("unchecked")
	private User getUserBytoken(String token) {

		Query query = em.createQuery("from " + User.class.getSimpleName() + " u where u.token = '" + token + "'");

		List<User> users = query.getResultList();
		if (users != null && users.size() > 0)
			return users.get(0);
		else
			return null;
	}

	private void updateUserToken(User user, String token) {
		user.setToken(token);
		this.em.merge(user);
	}

}

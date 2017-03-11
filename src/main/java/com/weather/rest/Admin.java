package com.weather.rest;

import java.util.List;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.weather.core.constants.Constants;
import com.weather.core.exception.WeatherException;
import com.weather.core.json.JsonHandler;
import com.weather.data.dao.NoteDao;
import com.weather.data.dao.UserDao;
import com.weather.data.filter.AuthenticateAsAdmin;
import com.weather.data.model.Note;
import com.weather.data.model.PredefinedNote;

@Path("/admin")
@Component
@Service
public class Admin
{

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   /***
    * User Dao.
    */
   @Autowired
   private UserDao userDao;

   @Autowired
   private NoteDao noteDao;

   @Autowired
   private JsonHandler jsonHandler;

	/***
	 * Login User Method is the entry point for admin login.
	 * @param email
	 * @param password
	 * @return JSON object represents the success/failure of the transaction in addition to token cookie
	 */
   @GET
   @Path("/login")
   @Produces(MediaType.APPLICATION_JSON)
   public Response login(@QueryParam(Constants.EMAIL) String email, @QueryParam(Constants.PASSWORD) String password)
   {

      logger.info("login User Method has been called");
      logger.debug("Parameters : email [{}], password [*****]", email);

      try
      {

         String token = userDao.login(email, password, true);

         NewCookie tokenCookie = new NewCookie(Constants.COOKIE_TOKEN_NAME, token);

         logger.info("User logged in successfully");

         return Response.status(200).cookie(tokenCookie).entity(jsonHandler.createSuccessJsonResponse()).build();

      }
      catch (WeatherException ex)
      {
         logger.error("Exception Occured while user trying to login  with error message [{}]", ex.getMessage());
         return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();
      }
   }

   /***
   * Logout User Method is the entry point for customer logout.
   *  
   * @param token cookie
   * @return JSON object represents the success/failure of the transaction
   */
   @GET
   @AuthenticateAsAdmin
   @Path("/logout")
   @Produces(MediaType.APPLICATION_JSON)
   public Response logout(@CookieParam(Constants.COOKIE_TOKEN_NAME) Cookie cookie)
   {

      logger.info("logout User Method has been called");
      logger.debug("Parameters : cookie [{}]", cookie);

      try
      {

         userDao.logout(cookie.getValue());

         logger.info("User logged out successfully");

         return Response.status(200).entity(jsonHandler.createSuccessJsonResponse()).build();

      }
      catch (WeatherException ex)
      {
         logger.error("Exception Occured while user trying to logout  with error message [{}]", ex.getMessage());
         return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();
      }
   }

   /***
    * create new note.
    * @param note object
    * @return JSON object represents the success/failure of the transaction
    */
   @POST
   @AuthenticateAsAdmin
   @Path("/create-note")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response createNote(Note note)
   {

      logger.info("Create Note Method has been called");
      logger.debug("Parameters : Note [{}]", note);

      try
      {

         noteDao.createNote(note);
         logger.info("Create Note done successfully");
         return Response.status(200).entity(jsonHandler.createSuccessJsonResponse()).build();

      }
      catch (WeatherException ex)
      {

         logger.error("Exception Occured while creating new note with error message [{}]", ex.getMessage());
         return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();

      }
   }

   /***
    * create new predefined note.
    * @param predefined note object
    * @return JSON object represents the success/failure of the transaction
    */   
   @POST
   @AuthenticateAsAdmin
   @Path("/create-predefined-note")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response createPredefinedNote(@QueryParam(Constants.NOTE_RANGE_ID) int rangeID, PredefinedNote note)
   {

      logger.info("Create Note Method has been called");
      logger.debug("Parameters : Note [{}]", note);

      try
      {

         noteDao.createPredefinedNote(rangeID, note);
         logger.info("Create Note done successfully");
         return Response.status(200).entity(jsonHandler.createSuccessJsonResponse()).build();

      }
      catch (WeatherException ex)
      {

         logger.error("Exception Occured while creating new predefined with error message [{}]", ex.getMessage());
         return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();

      }
   }

   /***
    * get all notes
    * @return JSON object represents the success/failure of the transaction in addition to notes 
    */
   @GET
   @AuthenticateAsAdmin
   @Path("/get-all-notes")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllNotes()
   {

      logger.info("get All Notes Method has been called");

      try
      {

         List<Note> notes = noteDao.getAllNotes();
         
         logger.info("Retrieved Nots {}", notes);

         return Response.status(200).entity(jsonHandler.createSuccessJsonResponseWithBody("notes", notes)).build();

      }
      catch (WeatherException ex)
      {
         logger.error("Exception Occured while user trying to get all notes with error message [{}]", ex.getMessage());
         return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();
      }
   }
   

   /***
    * get all predefined notes
    * @return JSON object represents the success/failure of the transaction in addition to predefined notes 
    */
   @GET
   @AuthenticateAsAdmin
   @Path("/get-all-predefined-notes")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllPredefinedNotes()
   {

      logger.info("get All Predefined Notes Method has been called");

      try
      {

         List<PredefinedNote> notes = noteDao.getAllPredefinedNotes();

         logger.info("Retrieved Nots {}", notes);
         
         return Response.status(200)
            .entity(jsonHandler.createSuccessJsonResponseWithBody(Constants.NOTES_ELEMENT, notes)).build();

      }
      catch (WeatherException ex)
      {
         logger.error("Exception Occured while user trying to get all predefined nots with error message [{}]", ex.getMessage());
         return Response.status(200).entity(jsonHandler.createJsonResponseFromException(ex)).build();
      }
   }

}

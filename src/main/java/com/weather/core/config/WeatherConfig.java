package com.weather.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/****
 * 
 * used to get the application configurations.
 * @author feteha
 *
 */
@Configuration
@PropertySources({@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
   @PropertySource(value = "file:${conf.file}", ignoreResourceNotFound = true)})
@Service
public class WeatherConfig
{

   @Autowired
   private Environment env;

   /**
    * Get the property value by property key.
    * @param key of property
    * @return String 
    */
   public String getProperty(String key)
   {
      return env.getProperty(key);
   }
}
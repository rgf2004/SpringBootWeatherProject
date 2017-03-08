package com.weather;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("rest")
public class JerseyConfig extends ResourceConfig {

	@Autowired
    ApplicationContext appCtx;
	
    @PostConstruct
    private void init() {
//       packages("com.weather").register(JacksonFeature.class);
    	
    	Map<String,Object> beans = appCtx.getBeansWithAnnotation(Path.class);
        for (Object o : beans.values()) {
            register(o);
        }
        
        register(JacksonFeature.class);
    	
    }
}

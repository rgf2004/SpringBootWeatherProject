package com.weather;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("rest")
public class JerseyConfig extends ResourceConfig {

    @PostConstruct
    private void init() {
       packages("com.weather").register(JacksonFeature.class);
    }
}

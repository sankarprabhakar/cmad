package com.cisco.cmad.blogs.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class BlogsApplication extends ResourceConfig {
    public BlogsApplication() {
        packages("com.cisco.cmad.blogs.rest");
    }
}

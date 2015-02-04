package com.farata.course.mwd.auction.engine.config;

import javax.ejb.Singleton;

/**
 * Created by ds on 04/02/15.
 */
@Singleton
public class MainConfig {

    // Set up all the default values
    public static final String DEFAULT_MESSAGE_COUNT = "57";
    public static final String DEFAULT_USERNAME = "quickstartUser";
    public static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    public static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    public static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";


}

package com.qsocialnow;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

public class App {

    private static final String DEFAULT_ENVIRONMENT = "development";

    private final ConfigurableApplicationContext context;

    private static App app;

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    private App() {
        String env = System.getenv("environment");
        if (env == null) {
            env = DEFAULT_ENVIRONMENT;
        }
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, env);
        context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        context.registerShutdownHook();
    }

    public Object getBean(String bean) {
        return context.getBean(bean);
    }

}

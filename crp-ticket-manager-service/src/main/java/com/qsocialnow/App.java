package com.qsocialnow;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    private final ConfigurableApplicationContext context;

    private static App app;

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    private App() {
        context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        context.registerShutdownHook();
    }

    public Object getBean(String bean) {
        return context.getBean(bean);
    }

}

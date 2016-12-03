package com.qsocialnow.autoscaling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.qsocialnow.autoscaling.processor.ElasticsearchProcessor;
import com.qsocialnow.autoscaling.service.HealthService;


public class App{
	
	private ElasticsearchProcessor elasticsearchProcessor;
	
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main( String[] args )
    {
       log.info("Starting autoscaling service" );
       ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(
               "classpath:spring/applicationContext.xml");
       context.registerShutdownHook();
    }

	public void start() {
		this.elasticsearchProcessor.execute();
    }

	public void close() {
        log.debug("Starting exit...");

    }

	public void setElasticsearchProcessor(ElasticsearchProcessor elasticsearchProcessor) {
		this.elasticsearchProcessor = elasticsearchProcessor;
	}
}

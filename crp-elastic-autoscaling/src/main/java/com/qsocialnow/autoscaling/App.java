package com.qsocialnow.autoscaling;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qsocialnow.autoscaling.processor.ElasticsearchProcessor;

public class App {

    private ElasticsearchProcessor elasticsearchProcessor;

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
    	if (!lock())
        {
    		log.error("Check that no other instance of elasticserch-autoscaling is running and if so, delete the file autoscaling.lock");
            return;
        }
    	log.info("Starting autoscaling service");
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
    
    private static boolean lock()
    {
      try
       {
           final File file=new File("autoscaling.lock");
           if (file.createNewFile())
           {
               file.deleteOnExit();
               return true;
           }
           return false;
       }
       catch (IOException e)
       {
           return false;
       }
   }
    
    
}

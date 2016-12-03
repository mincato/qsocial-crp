package com.qsocialnow.autoscaling.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement; 
import java.util.Map.Entry;

import javax.sound.midi.MidiDevice.Info;

import com.qsocialnow.autoscaling.config.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.autoscaling.config.Configurator;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.cluster.NodesInfo;
import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

public class ElasticsearchClient{
    
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchClient.class);

    private AWSElasticsearchConfigurationProvider config;

    private JestClient client;

    private AWSSigningRequestInterceptor requestInterceptor;

    public ElasticsearchClient(AWSElasticsearchConfigurationProvider config) {
        this.config = config;
    }

	public void initClient() {
		if (client == null) {

			AWSSigner signer = getSigner();
			requestInterceptor = new AWSSigningRequestInterceptor(signer);
			JestClientFactory factory = new JestClientFactory() {
				@Override
				protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
					builder.addInterceptorLast(requestInterceptor);
					return builder;
				}

				@Override
				protected HttpAsyncClientBuilder configureHttpClient(HttpAsyncClientBuilder builder) {
					builder.addInterceptorLast(requestInterceptor);
					return builder;
				}
			};
			factory.setHttpClientConfig(
					new HttpClientConfig.Builder(this.config.getEndpoint()).multiThreaded(true).build());
			this.client = factory.getObject();
		}
	}

    public void closeClient() {
        if (client == null) {
            client.shutdownClient();
        }
    }

    private AWSSigner getSigner() {
        Supplier<LocalDateTime> clock = () -> LocalDateTime.now(ZoneOffset.UTC);
        AWSSigner awsSigner = new AWSSigner(this.config, this.config.getRegion(), Configurator.SERVICE_NAME, clock);
        return awsSigner;
    }
    

    public void checkClusterHealth() {
        try {
        	NodesInfo nodesInfo = new NodesInfo.Builder().addCleanApiParameter("stats").build();
        	JestResult result  = client.execute(nodesInfo);
            
            if (result.isSucceeded()) {
                LinkedHashSet<String> httpHosts = new LinkedHashSet<String>();

                JsonObject jsonMap = result.getJsonObject();
                JsonObject nodes = (JsonObject) jsonMap.get("nodes");
                if (nodes != null) {
                    for (Entry<String, JsonElement> entry : nodes.entrySet()) {
                        JsonObject host = entry.getValue().getAsJsonObject();
                        JsonObject indices = (JsonObject) host.get("indices");
                        
                        log.info("indices: "+indices.toString());
                    }
                }
                log.info("Discovered {} HTTP hosts: {}", httpHosts.size(), StringUtils.join(httpHosts, ","));
            } else {
                log.warn("NodesInfo request resulted in error: {}", result.getErrorMessage());
            }
            
        } catch (IOException e) {
            log.error("Unexpected error: ", e);

        }
    }

 


}

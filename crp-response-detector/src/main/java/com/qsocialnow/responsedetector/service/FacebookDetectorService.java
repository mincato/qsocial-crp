package com.qsocialnow.responsedetector.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.common.util.StringUtils;
import com.qsocialnow.responsedetector.config.FacebookConfigurator;
import com.qsocialnow.responsedetector.config.ResponseDetectorConfig;
import com.qsocialnow.responsedetector.factories.FacebookConfiguratorFactory;
import com.qsocialnow.responsedetector.model.FacebookFeedEvent;
import com.qsocialnow.responsedetector.model.TwitterMessageEvent;
import com.qsocialnow.responsedetector.sources.FacebookClient;
import com.qsocialnow.responsedetector.sources.FacebookFeedConsumer;
import com.qsocialnow.responsedetector.sources.TwitterClient;

public class FacebookDetectorService extends SourceDetectorService {

    private static final Logger log = LoggerFactory.getLogger(FacebookDetectorService.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private ResponseDetectorConfig appConfig;

    private TreeCache treeCache;

    @Autowired
    private FacebookConfiguratorFactory facebookConfiguratorFactory;

    @Autowired
    private EventProcessor eventProcessor;

    private FacebookClient facebookClient;

    private FacebookConfigurator configurator;
    
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    
    private FacebookFeedConsumer facebookFeedConsumer;

    private boolean startListening = false;

    private HashMap<String, HashSet<FacebookFeedEvent>> conversations;

    @Override
    public void run() {

        try {

            configurator = facebookConfiguratorFactory.getConfigurator(appConfig.getFacebookAppConfiguratorZnodePath());

            facebookClient = new FacebookClient(configurator);
            facebookClient.initClient();
            facebookFeedConsumer = new FacebookFeedConsumer(facebookClient);

            conversations = new HashMap<String, HashSet<FacebookFeedEvent>>();
            
            treeCache = new TreeCache(zookeeperClient, appConfig.getFacebookUsersZnodePath());

            addListener();
            treeCache.start() ; //start(StartMode.POST_INITIALIZED_EVENT);
            startFeedConsumer();
            
        } catch (Exception e) {
            log.error("There was an unexpected error", e);
            System.exit(1);
        }
    }

    private void addListener() {
    	TreeCacheListener listener = new TreeCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event)
					throws Exception {
				switch (event.getType()) {
				case NODE_ADDED: {
					 System.out.println("TreeNode added: "
							+ ZKPaths
									.getNodeFromPath(event.getData().getPath())
							+ ", value: "
							+ new String(event.getData().getData()));

					 String nodeAdded = ZKPaths.getNodeFromPath(event.getData().getPath());
					 log.info("Adding node:" + nodeAdded);
					 if(event.getData().getData()!=null){    
						 String nodeValue = new String(event.getData().getData());
						 log.info("Adding node value:-" + nodeValue+"-");
						 if(nodeValue.equals("NEW")){
							 addUserResolverTrack(nodeAdded);
						 }
						 else{
							 //new conversation
							 if(!Strings.isNullOrEmpty(nodeValue)){
								 FacebookFeedEvent facebookFeedEvent = new GsonBuilder().create().fromJson(nodeValue, FacebookFeedEvent.class);
	                             if (facebookFeedEvent != null) {
	                            	 addFacebookFeedEvent(nodeAdded,facebookFeedEvent);
	                             }
							 }
							 else{
								 log.info("Not Adding node with empty value ");
							 }
						 }
					 }else{
						 log.info("Not Adding node value");
					 }
					break;
				}
				case NODE_UPDATED: {
					System.out.println("TreeNode changed: "
							+ ZKPaths
									.getNodeFromPath(event.getData().getPath())
							+ ", value: "+new String(event.getData().getData()));
						
					
					break;
				}
				case NODE_REMOVED: {
					System.out
							.println("TreeNode removed: "
									+ ZKPaths.getNodeFromPath(event.getData()
											.getPath()));
					
					removeSourceConversation("","");
					break;
				}
				case INITIALIZED:{

					
					startListening = true;
					break;
				}
				default:
					System.out
							.println("Other event: " + event.getType().name());
				}
			}

		};
        treeCache.getListenable().addListener(listener);
    }

    private void addUserResolverTrack(String userResolverToFilter) {
        try {
            if (startListening) {
                log.info("Adding UserResolver:" + userResolverToFilter);
            }
        } catch (Exception e) {
            log.error("There was an error adding new User Resolver to track :" + userResolverToFilter, e);
        }
    }

    private void addFacebookFeedEvent(String conversationId, FacebookFeedEvent facebookFeedEvent) {
        try {
            if (startListening) {
                HashSet<FacebookFeedEvent> conversationsByUserResolver = conversations.get(conversationId);
                if (conversationsByUserResolver == null) {
                    conversationsByUserResolver = new HashSet<FacebookFeedEvent>();
                    log.info("Init conversation list from user :" + conversationId);
                }
                if (!conversationsByUserResolver.contains(facebookFeedEvent)) {
                    log.info("Adding message conversation from :" + conversationId + " from Case:"
                            + facebookFeedEvent.getCaseId());
                    conversationsByUserResolver.add(facebookFeedEvent);
                }
                conversations.put(conversationId, conversationsByUserResolver);
            }
        } catch (Exception e) {
            log.error("There was an error adding converstaions from userResolver: " + conversationId, e);
        }
    }

    private void checkMessageResponses(String userResolver, TwitterMessageEvent[] messages) {
        TwitterClient twitterClient = new TwitterClient(this);
        //twitterClient.initTwitterClient(configurator);
        for (int i = 0; i < messages.length; i++) {
            twitterClient.checkAnyMention(userResolver, messages[i]);
        }
    }

    public void stop() {
        if (facebookClient != null) {
            this.facebookClient.stop();
        }
        try {
            if (treeCache != null) {
                treeCache.close();
            }
        } catch (Exception e) {
            log.error("Unexpected error. Cause", e);
        }
    }

    @Override
    public void removeSourceConversation(String userResolver, String converstation) {
        try {
            
        	String status = "NEW";
        	byte[] messageBytes = status.getBytes("UTF8");
        	
        	//zookeeperClient.setData().forPath(appConfig.getFacebookUsersZnodePath() + "/centaurico_test",messageBytes);

        } catch (Exception e) {
            log.error("Unable to remove message conversation: " + converstation, e);
        }
    }
    
    private void startFeedConsumer() {
        log.info("Starting Feed Consumer to check new comments from existing posts");
        executor.scheduleWithFixedDelay(facebookFeedConsumer, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void processEvent(Boolean isResponseFromMessage, String userResolver, String[] userMentions,
            String sourceMessageId, String messageText, String inReplyToMessageId, String userId, String userName,
            String userProfileImage) {

        try {
            InPutBeanDocument event = new InPutBeanDocument();
            String mainUserResolver = null;
            event.setId(sourceMessageId);
            event.setFecha(new Date());
            // event.setTipoDeMedio("morbi");
            event.setName(messageText);
            event.setTitulo(messageText);
            event.setTexto(messageText);
            event.setNormalizeMessage(messageText);

            if (userMentions != null) {
                for (int i = 0; i < userMentions.length; i++) {
                    String userMention = userMentions[i];
                    if (conversations.containsKey(userMention)) {
                        mainUserResolver = userMention;
                        break;
                    }
                }
            }

            if (isResponseFromMessage) {
                Set<FacebookFeedEvent> conversationsByUserResolver = conversations.get(userResolver);
                if (conversationsByUserResolver != null) {
                    for (FacebookFeedEvent twitterMessageEvent : conversationsByUserResolver) {
                       /* if (twitterMessageEvent.getReplyMessageId().equals(inReplyToMessageId)) {
                            // is response from existing conversation
                            event.setIdPadre(twitterMessageEvent.getEventId());
                            event.setOriginIdCase(twitterMessageEvent.getCaseId());
                            conversationsByUserResolver.remove(twitterMessageEvent);
                            break;
                        }*/
                    }
                }
            } else {
                Set<FacebookFeedEvent> conversationsByUserResolver = conversations.get(mainUserResolver);
                if (conversationsByUserResolver != null) {
                    for (FacebookFeedEvent twitterMessageEvent : conversationsByUserResolver) {
                      /*  if (twitterMessageEvent.getUserId().equals(userId)) {
                            // is response from existing user
                            event.setIdPadre(twitterMessageEvent.getEventId());
                            event.setOriginIdCase(twitterMessageEvent.getCaseId());
                            conversationsByUserResolver.remove(twitterMessageEvent);
                            break;
                        }*/
                    }
                }
            }
            event.setUsuarioOriginal(userName);
            event.setIdUsuarioOriginal(userId);
            event.setProfileImage(userProfileImage);

            event.setResponseDetected(true);
            event.setFechaCreacion(new Date());
            eventProcessor.process(event);
            log.info("Creating event to handle automatic response detection");
        } catch (Exception e) {
            log.error("Error trying to register event :" + e);
        }
    }
}

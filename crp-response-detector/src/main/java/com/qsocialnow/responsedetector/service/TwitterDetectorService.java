package com.qsocialnow.responsedetector.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.common.model.event.InPutBeanDocumentLocation;
import com.qsocialnow.responsedetector.config.ResponseDetectorConfig;
import com.qsocialnow.responsedetector.config.TwitterConfigurator;
import com.qsocialnow.responsedetector.factories.TwitterConfiguratorFactory;
import com.qsocialnow.responsedetector.model.TwitterMessageEvent;
import com.qsocialnow.responsedetector.sources.TwitterClient;
import com.qsocialnow.responsedetector.sources.TwitterStatusListener;
import com.qsocialnow.responsedetector.sources.TwitterStreamClient;

import twitter4j.Status;
import twitter4j.UserMentionEntity;

public class TwitterDetectorService extends SourceDetectorService {

    private static final Logger log = LoggerFactory.getLogger(TwitterDetectorService.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private ResponseDetectorConfig appConfig;

    private PathChildrenCache pathChildrenCache;

    @Autowired
    private TwitterConfiguratorFactory twitterConfiguratorFactory;

    @Autowired
    private EventProcessor eventProcessor;

    private TwitterStreamClient twitterStreamClient;

    private TwitterConfigurator configurator;

    private boolean startListening = false;

    @Override
    public void run() {

        try {
            configurator = twitterConfiguratorFactory.getConfigurator(appConfig.getTwitterAppConfiguratorZnodePath());
            twitterStreamClient = new TwitterStreamClient(configurator);
            twitterStreamClient.initClient();
            twitterStreamClient.start();

            pathChildrenCache = new PathChildrenCache(zookeeperClient, appConfig.getTwitterMessagesPath(), true);
            addListener();
            pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);

        } catch (Exception e) {
            log.error("There was an unexpected error", e);
            System.exit(1);
        }
    }

    private void addListener() {
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {

            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        log.info("Message added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        byte[] messageBytes = event.getData().getData();
                        TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                new String(messageBytes), TwitterMessageEvent.class);
                        createMessageHandlerProcessor(twitterMessageEvent);
                        break;
                    }
                    case INITIALIZED: {
                        log.info("PathChildrenCache initialized");
                        List<ChildData> initialData = event.getInitialData();
                        for (ChildData childData : initialData) {
                            byte[] messageBytes = childData.getData();
                            TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                    new String(messageBytes), TwitterMessageEvent.class);
                            checkMessageResponses(twitterMessageEvent);
                            log.info("initial node-message: " + twitterMessageEvent.getMessageId());
                        }
                        startListening = true;
                        break;
                    }
                    case CHILD_UPDATED: {
                        log.info("Node-Message updated: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        byte[] messageBytes = event.getData().getData();
                        TwitterMessageEvent twitterMessageEvent = new GsonBuilder().create().fromJson(
                                new String(messageBytes), TwitterMessageEvent.class);
                        createMessageHandlerProcessor(twitterMessageEvent);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
    }

    private void createMessageHandlerProcessor(TwitterMessageEvent message) {
        try {
            if (startListening) {
                log.info("Adding message:" + message.getMessageId() + "from Case:" + message.getCaseId());
                twitterStreamClient.addListeners(new TwitterStatusListener(this, twitterStreamClient, message));
            }
        } catch (Exception e) {
            log.error("There was an error creating the message handler processor for tweet: " + message.getMessageId(),
                    e);
        }
    }

    private void checkMessageResponses(TwitterMessageEvent message) {
        TwitterClient twitterClient = new TwitterClient(this);
        twitterClient.initTwitterClient(configurator);
        twitterClient.checkAnyMention(message);
    }

    public void stop() {
        if (twitterStreamClient != null) {
            this.twitterStreamClient.stop();
        }
        try {
            if (pathChildrenCache != null) {
                pathChildrenCache.close();
            }
        } catch (IOException e) {
            log.error("Unexpected error. Cause", e);
        }
    }

    @Override
    public void removeSourceConversation(String converstation) {
        try {
            zookeeperClient.delete().forPath(appConfig.getTwitterMessagesPath() + "/" + converstation);
        } catch (Exception e) {
            log.error("Unable to remove message conversation:: " + converstation, e);
        }
    }

    @Override
    public void processEvent(TwitterMessageEvent messageEvent, Status status) {
        try {
            InPutBeanDocument event = new InPutBeanDocument();
            event.setTokenId(2L);
            Long[] series = { 33L, 7L };
            event.setSeriesId(series);
            Long[] subSeries = { 9L };
            event.setSubSeriesId(subSeries);

            event.setId("non");
            event.setIdOriginal("odio");
            event.setTipoDeMedio("morbi");
            event.setConnotacion((short) 36);
            event.setSeccionExternaOrigen(72L);
            event.setSistemaOrigenExterno("ac");
            event.setFecha(new Date());
            event.setEsTapa(false);
            event.setUsuarioOriginal("smoore0");
            event.setUsuarioCreacion("sdixon0");
            event.setUsuarioReproduccion("sbowman0");
            event.setUsuarioCategorizador("sadams0");
            event.setFechaCategorizacion(new Date());
            event.setNoticiaStatus(1);
            event.setOdaCategorizada(23);
            event.setEsSecundario(false);
            event.setIdPadre(messageEvent.getEventId());
            event.setEsLike(false);
            event.setEsReproduccion(false);
            event.setFechaCreacion(new Date());
            event.setVersionDiccionarioActores(new Date());
            event.setVersionDiccionarioActores(new Date());
            event.setVersionTemas(new Date());
            event.setReproduccionesCount(Long.valueOf(status.getRetweetCount()));
            event.setLikeCount(0L);
            event.setTitulo("Response detected from twitter source");
            event.setTexto(status.getText());
            event.setNormalizeMessage(status.getText());
            event.setUrlNoticia("");
            event.setUrlMultimediaContent("");
            event.setVolanta("");
            event.setBajada("");
            event.setCopete("");
            event.setFollowersCount(Long.valueOf(status.getFavoriteCount()));
            event.setIdUsuarioOriginal("platea");
            event.setIdUsuarioReproduccion("cubilia");
            event.setIdUsuarioCreacion("pretium");
            event.setResponseDetected(true);
            event.setProfileImage(status.getUser().getProfileImageURL());
            event.setName("Tweet response :" + status.getInReplyToScreenName() + " ");
            event.setLanguage(status.getLang());
            event.setOriginalLocation("donec");
            InPutBeanDocumentLocation location = new InPutBeanDocumentLocation();
            event.setLocation(location);
            event.setLocationMethod(null);

            UserMentionEntity[] twitterMentions = status.getUserMentionEntities();
            List<String> mentions = new ArrayList<>();
            for (int i = 0; i < twitterMentions.length; i++) {
                mentions.add(twitterMentions[i].getText());
            }
            event.setMenciones(null);
            event.setActores(null);
            Long[] areasTematicas = new Long[] { 19L, 23L, 36L };
            event.setAreasTematicas(areasTematicas);
            String[] hotTopics = new String[] { "luctus", "lectus", "a" };
            event.setHotTopics(hotTopics);
            Long[] temas = new Long[] { 95L, 1L, 8L };
            event.setTemas(temas);
            Long[] categorias = new Long[] { 55L, 73L, 66L };
            event.setCategorias(categorias);
            Long[] conjuntos = new Long[] { 15L, 66L, 100L };
            event.setConjuntos(conjuntos);
            event.setAtributosActores(null);
            event.setContinent(31L);
            event.setCountry(63L);
            event.setAdm1(22L);
            event.setAdm2(null);
            event.setAdm3(63L);
            event.setAdm4(null);
            event.setCity(28L);
            event.setNeighborhood(29L);
            event.setResponseDetected(true);
            event.setOriginIdCase(messageEvent.getCaseId());
            eventProcessor.process(event);
            log.info("Creating event to handle automatic response detection");
        } catch (Exception e) {
            log.error("Error trying to register event :" + e);
        }
    }
}

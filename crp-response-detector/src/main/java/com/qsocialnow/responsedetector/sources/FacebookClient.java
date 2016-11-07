package com.qsocialnow.responsedetector.sources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.responsedetector.config.FacebookConfigurator;
import com.qsocialnow.responsedetector.service.SourceDetectorService;

import facebook4j.BatchRequest;
import facebook4j.BatchRequests;
import facebook4j.BatchResponse;
import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;
import facebook4j.internal.http.RequestMethod;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;
import facebook4j.json.DataObjectFactory;

public class FacebookClient {

    private ConfigurationBuilder configurationBuilder;

    private static final Logger log = LoggerFactory.getLogger(FacebookClient.class);

    private Facebook facebook;

    private List<String> messages;

    private SourceDetectorService sourceService;

    public FacebookClient(FacebookConfigurator facebookConfigurator, SourceDetectorService sourceDetectorService) {

        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthAppId(facebookConfigurator.getOAuthAppId())
                .setOAuthAppSecret(facebookConfigurator.getOAuthAppSecret())
                .setOAuthAccessToken(facebookConfigurator.getOAuthAccessToken());

        messages = new ArrayList<String>();
        this.sourceService = sourceDetectorService;
    }

    public void initClient() {
        facebook = new FacebookFactory(configurationBuilder.build()).getInstance();
    }

    public void removeConversation(String commentId) {
        messages.remove(commentId);
    }

    public void addNewConversation(String commentId) {
        messages.add(commentId);
    }

    public void readComments() {
        BatchRequests<BatchRequest> batch = new BatchRequests<BatchRequest>();

        for (String messageId : messages) {
            batch.add(new BatchRequest(RequestMethod.GET, messageId + "/comments"));
        }

        List<BatchResponse> results;
        try {
            if (batch.size() > 0) {
                results = facebook.executeBatch(batch);
                int indexMessage = 0;

                for (BatchResponse batchResponse : results) {
                    int status = batchResponse.getStatusCode();
                    String contentType = batchResponse.getResponseHeader("Content-Type");
                    log.debug("Status: " + status + " Content-Type: " + contentType);

                    JSONObject jsonObject = batchResponse.asJSONObject();
                    JSONArray comments = jsonObject.getJSONArray("data");

                    String commentId = messages.get(indexMessage);
                    boolean startToTrackComment = false;

                    if (comments.length() > 0) {
                        String idCommentToTrack = sourceService.getReplyIdToTrack(commentId);
                        String userToTrack = sourceService.getUserIdToTrack(commentId);

                        log.debug("Retrieving :" + comments.length() + " comments from rootReply:" + commentId);

                        if (comments != null && comments.length() == 1) {
                            startToTrackComment = true;
                        }

                        for (int i = 0; i < comments.length(); i++) {

                            JSONObject jsonComment = comments.getJSONObject(i);
                            Comment comment = DataObjectFactory.createComment(jsonComment.toString());

                            log.debug("Comment: " + comment.getId() + " from:" + comment.getFrom().getName()
                                    + " parent: " + comment.getParent() + " message:" + comment.getMessage());

                            if (startToTrackComment) {
                                if (comment.getFrom().getId().equals(userToTrack)) {
                                    log.debug("Finding user:" + userToTrack + " and comment:" + comment.getId()
                                            + "from conversation rootReply:" + commentId);
                                    Date createdTime = comment.getCreatedTime();
                                    sourceService.processEvent(true, createdTime.getTime(), null, null,
                                            comment.getId(), comment.getMessage(), commentId,
                                            comment.getFrom().getId(), comment.getFrom().getName(), null);

                                    removeConversation(commentId);
                                    break;
                                }
                            }
                            if (!startToTrackComment && comment.getId().equals(idCommentToTrack)) {
                                startToTrackComment = true;
                                log.debug("Init to Track comment from response: " + commentId);
                            }
                        }
                    }
                    indexMessage++;
                }
            }
        } catch (FacebookException | JSONException e) {
            log.error("Error reading comments from page", e);
        }
    }

    public void stop() {
        facebook.shutdown();
    }
}

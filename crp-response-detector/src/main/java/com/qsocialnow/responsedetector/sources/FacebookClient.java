package com.qsocialnow.responsedetector.sources;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.responsedetector.config.FacebookConfigurator;
import com.qsocialnow.responsedetector.service.FacebookDetectorService;

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

    private LinkedHashSet<String> messages;

    private FacebookDetectorService sourceService;

    public FacebookClient(FacebookConfigurator facebookConfigurator, FacebookDetectorService sourceDetectorService) {

        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthAppId(facebookConfigurator.getOAuthAppId())
                .setOAuthAppSecret(facebookConfigurator.getOAuthAppSecret())
                .setOAuthAccessToken(facebookConfigurator.getOAuthAccessToken());

        messages = new LinkedHashSet<String>();
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
            batch.add(new BatchRequest(RequestMethod.GET, messageId + "/comments?filter=stream"));
        }

        List<BatchResponse> results;
        try {
            if (batch.size() > 0) {
                results = facebook.executeBatch(batch);
                int indexMessage = 0;

                for (String rootCommentId : messages) {
                    BatchResponse batchResponse = results.get(indexMessage);
                    int status = batchResponse.getStatusCode();
                    String contentType = batchResponse.getResponseHeader("Content-Type");
                    log.debug("Status: " + status + " Content-Type: " + contentType);

                    JSONObject jsonObject = batchResponse.asJSONObject();
                    JSONArray comments = jsonObject.getJSONArray("data");

                    if (comments.length() > 0) {
                        List<String> idCommentsToTrack = sourceService.getReplyIdsToTrack(rootCommentId);
                        List<String> usersToTrack = sourceService.getUserIdsToTrack(rootCommentId);
                        boolean[] startToTrackComments = new boolean[idCommentsToTrack.size()];

                        log.debug("Retrieving :" + comments.length() + " comments from rootReply:" + rootCommentId);

                        if (comments != null && comments.length() == 1) {
                            Arrays.fill(startToTrackComments, true);
                        }

                        for (int i = 0; i < comments.length(); i++) {

                            JSONObject jsonComment = comments.getJSONObject(i);
                            Comment comment = DataObjectFactory.createComment(jsonComment.toString());

                            log.debug("Comment: " + comment.getId() + " from: id: " + comment.getFrom().getId()
                                    + " from name:" + comment.getFrom().getName() + " parent: " + comment.getParent()
                                    + " message:" + comment.getMessage());

                            for (int j = 0; j < idCommentsToTrack.size(); j++) {
                                String idCommentToTrack = idCommentsToTrack.get(j);
                                String userToTrack = usersToTrack.get(j);
                                boolean startToTrackComment = startToTrackComments[j];
                                startToTrackComment = startToTrackComment || rootCommentId.equals(idCommentToTrack);
                                if (startToTrackComment) {
                                    log.debug("Finding user:" + userToTrack + " and comment:" + comment.getId()
                                            + "from conversation rootReply:" + rootCommentId);

                                    log.debug("Comment user:" + comment.getFrom().getId());
                                    if (comment.getFrom().getId().equals(userToTrack)) {
                                        Date createdTime = comment.getCreatedTime();
                                        sourceService.processEvent(true, createdTime.getTime(), null, null, comment
                                                .getId(), comment.getMessage(), rootCommentId, j, comment.getFrom()
                                                .getId(), comment.getFrom().getName(), null);
                                    }
                                }
                                if (!startToTrackComment && comment.getId().equals(idCommentToTrack)) {
                                    startToTrackComments[j] = true;
                                    log.debug("Init to Track comment from response: " + rootCommentId);
                                }
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

package com.qsocialnow.responsedetector.sources;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.responsedetector.service.SourceDetectorService;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserMentionEntity;
import twitter4j.UserStreamListener;

public class ResponseDetectorStreamListener implements UserStreamListener {

    private SourceDetectorService sourceService;

    public ResponseDetectorStreamListener(SourceDetectorService sourceDetectorService) {
        this.sourceService = sourceDetectorService;
    }

    private static final Logger log = LoggerFactory.getLogger(ResponseDetectorStreamListener.class);

    @Override
    public void onStatus(Status status) {
        log.debug("onStatus @ screenName: " + status.getUser().getScreenName() + " messageId: " + status.getId()
                + " id:" + status.getUser().getId() + "name: " + status.getUser().getName() + " original profile: "
                + status.getUser().getOriginalProfileImageURLHttps() + " profile image: "
                + status.getUser().getProfileImageURL() + " text: " + status.getText() + " isRetweet: "
                + status.isRetweet() + " inReplyToStatusId: " + status.getInReplyToStatusId()
                + " currentUserRetweetId: " + status.getCurrentUserRetweetId() + " inReplyToScrenName: "
                + status.getInReplyToScreenName() + " inReplyToUSerId: " + status.getInReplyToUserId()
                + " quotedStatusId: " + status.getQuotedStatusId() + " rateLimit: " + status.getRateLimitStatus());

        String[] userMentions = null;

        UserMentionEntity[] mentions = status.getUserMentionEntities();
        if (mentions != null) {
            userMentions = new String[mentions.length];
            for (int i = 0; i < mentions.length; i++) {
                UserMentionEntity userMentionEntity = mentions[i];
                userMentions[i] = userMentionEntity.getScreenName();
                log.debug("User Mentions: " + userMentions[i]);
            }
        }

        boolean isRetweet = false;
        String userResolver = null;

        if (status.getInReplyToStatusId() > 0) {
            isRetweet = true;
            userResolver = status.getInReplyToScreenName();
        }
        Date created = status.getCreatedAt();
        sourceService.processEvent(isRetweet, created.getTime(), userResolver, userMentions, String.valueOf(status
                .getId()), status.getText(), String.valueOf(status.getInReplyToStatusId()), String.valueOf(status
                .getUser().getId()), status.getUser().getName(), status.getUser().getOriginalProfileImageURLHttps());
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        log.debug("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

    @Override
    public void onDeletionNotice(long directMessageId, long userId) {
        log.debug("Got a direct message deletion notice id:" + directMessageId);
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        log.debug("Got a track limitation notice:" + numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        log.debug("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        log.debug("Got stall warning:" + warning);
    }

    @Override
    public void onFriendList(long[] friendIds) {

    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
        log.debug("onFavorite source:@" + source.getScreenName() + " target:@" + target.getScreenName() + " @"
                + favoritedStatus.getUser().getScreenName() + " - " + favoritedStatus.getText());
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        log.debug("onUnFavorite source:@" + source.getScreenName() + " target:@" + target.getScreenName() + " @"
                + unfavoritedStatus.getUser().getScreenName() + " - " + unfavoritedStatus.getText());
    }

    @Override
    public void onFollow(User source, User followedUser) {
        log.debug("onFollow source:@" + source.getScreenName() + " target:@" + followedUser.getScreenName());
    }

    @Override
    public void onUnfollow(User source, User followedUser) {
        System.out.println("onFollow source:@" + source.getScreenName() + " target:@" + followedUser.getScreenName());
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        System.out.println("onDirectMessage text:" + directMessage.getText());
    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberAddition added member:@" + addedMember.getScreenName() + " listOwner:@"
                + listOwner.getScreenName() + " list:" + list.getName());
    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberDeleted deleted member:@" + deletedMember.getScreenName() + " listOwner:@"
                + listOwner.getScreenName() + " list:" + list.getName());
    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListSubscribed subscriber:@" + subscriber.getScreenName() + " listOwner:@"
                + listOwner.getScreenName() + " list:" + list.getName());
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListUnsubscribed subscriber:@" + subscriber.getScreenName() + " listOwner:@"
                + listOwner.getScreenName() + " list:" + list.getName());
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list) {
        System.out.println("onUserListCreated  listOwner:@" + listOwner.getScreenName() + " list:" + list.getName());
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list) {
        System.out.println("onUserListUpdated  listOwner:@" + listOwner.getScreenName() + " list:" + list.getName());
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list) {
        System.out.println("onUserListDestroyed  listOwner:@" + listOwner.getScreenName() + " list:" + list.getName());
    }

    @Override
    public void onUserProfileUpdate(User updatedUser) {
        System.out.println("onUserProfileUpdated user:@" + updatedUser.getScreenName());
    }

    @Override
    public void onUserDeletion(long deletedUser) {
        System.out.println("onUserDeletion user:@" + deletedUser);
    }

    @Override
    public void onUserSuspension(long suspendedUser) {
        System.out.println("onUserSuspension user:@" + suspendedUser);
    }

    @Override
    public void onBlock(User source, User blockedUser) {
        System.out.println("onBlock source:@" + source.getScreenName() + " target:@" + blockedUser.getScreenName());
    }

    @Override
    public void onUnblock(User source, User unblockedUser) {
        System.out.println("onUnblock source:@" + source.getScreenName() + " target:@" + unblockedUser.getScreenName());
    }

    @Override
    public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
        System.out.println("onRetweetedRetweet source:@" + source.getScreenName() + " target:@"
                + target.getScreenName() + retweetedStatus.getUser().getScreenName() + " - "
                + retweetedStatus.getText());
    }

    @Override
    public void onFavoritedRetweet(User source, User target, Status favoritedRetweet) {
        System.out.println("onFavroitedRetweet source:@" + source.getScreenName() + " target:@"
                + target.getScreenName() + favoritedRetweet.getUser().getScreenName() + " - "
                + favoritedRetweet.getText());
    }

    @Override
    public void onQuotedTweet(User source, User target, Status quotingTweet) {
        System.out.println("onQuotedTweet" + source.getScreenName() + " target:@" + target.getScreenName()
                + quotingTweet.getUser().getScreenName() + " - " + quotingTweet.getText());
    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
        System.out.println("onException:" + ex.getMessage());
    }
}

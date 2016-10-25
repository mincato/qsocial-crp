package com.qsocialnow.common.util;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.event.Event;

public class DeepLinkBuilder {

    public static String build(Event event) {
        if (Media.TWITTER.getValue().equals(event.getMedioId())) {
            String creationUser = formatCreationUser(event.getUsuarioCreacion());
            return MessageFormat.format("https://twitter.com/{0}/status/{1}", creationUser, event.getId());
        }
        if (Media.FACEBOOK.getValue().equals(event.getMedioId())) {
            String id = event.getId();
            String originalId = event.getIdOriginal();
            String rootCommentId = event.getRootCommentId();

            String[] splittedId = id.split("_");

            if (StringUtils.isBlank(originalId)) {
                return MessageFormat.format("https://facebook.com/{0}/posts/{1}", splittedId[0], splittedId[1]);
            }

            String[] splittedOriginalId = originalId.split("_");

            if (StringUtils.isBlank(rootCommentId)) {
                return MessageFormat.format("https://facebook.com/{0}/posts/{1}?comment_id={2}", splittedOriginalId[0],
                        splittedOriginalId[1], splittedId[1]);
            }

            String[] splittedRootCommentId = rootCommentId.split("_");

            return MessageFormat.format("https://facebook.com/{0}/posts/{1}?comment_id={2}&reply_comment_id={3}",
                    splittedOriginalId[0], splittedOriginalId[1], splittedRootCommentId[1], splittedId[1]);
        }
        return null;
    }

    private static String formatCreationUser(String creationUser) {
        int firstBlankPosition = creationUser.indexOf(" ");
        if (firstBlankPosition != -1) {
            return creationUser.substring(0, firstBlankPosition);
        }
        return creationUser;
    }
}

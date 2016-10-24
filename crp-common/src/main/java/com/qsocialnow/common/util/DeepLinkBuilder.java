package com.qsocialnow.common.util;

import java.text.MessageFormat;

import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.event.Event;

public class DeepLinkBuilder {

	public static String build(Event event) {
		if (Media.TWITTER.getValue().equals(event.getMedioId())) {
			return MessageFormat.format("https://twitter.com/{0}/status/{1}", event.getUsuarioCreacion(),
					event.getId());
		}
		if (Media.FACEBOOK.getValue().equals(event.getMedioId())) {
			String[] splittedId = event.getId().split("_");
			String[] splittedOriginalId = event.getIdOriginal().split("_");
			String fatherId = event.getIdPadre();
			return MessageFormat.format("https://facebook.com/{0}/posts/{1}?commentId={2}&reply_comment_id={3}", splittedOriginalId[0],
					splittedOriginalId[1], fatherId, splittedId[1]);
		}
		return null;
	}
}

package com.qsocialnow.eventresolver.filters.matchers;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("authorWordFilterMatcher")
public class AuthorWordFilterMatcher implements WordFilterMatcher {

    private static final String AUTHOR_REGEX = "{0} http.*|{0}";

    @Override
    public boolean match(NormalizedInputBeanDocument message, Set<String> filterTexts) {
        return match(message.getUsuarioCreacion(), filterTexts) || match(message.getUsuarioReproduccion(), filterTexts);

    }

    private boolean match(String author, Set<String> filterAuthors) {
        boolean match = false;
        if (author != null) {
            String messageAuthor = author;
            Iterator<String> it = filterAuthors.iterator();
            while (!match && it.hasNext()) {
                String filterAuthor = it.next();
                String regex = MessageFormat.format(AUTHOR_REGEX, filterAuthor);
                match = Pattern.matches(regex, messageAuthor);
            }
        }

        return match;
    }

}

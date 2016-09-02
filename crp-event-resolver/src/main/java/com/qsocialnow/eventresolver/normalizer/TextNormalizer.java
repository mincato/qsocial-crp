package com.qsocialnow.eventresolver.normalizer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.util.StringUtils;

@Component
public class TextNormalizer {

    private static Pattern DIGITS_PATTERN = Pattern.compile("^(\\d)+$");

    private static Logger log = LoggerFactory.getLogger(TextNormalizer.class);

    public Set<String> normalize(String text) {
        Set<String> words = new HashSet<>();
        Analyzer analyzer = null;
        TokenStream tokenStream = null;
        try {
            InputStream stopWords = ClassLoader.getSystemResourceAsStream("stopwords.txt");
            Reader reader = new InputStreamReader(stopWords);
            analyzer = new StandardAnalyzer(reader);
            tokenStream = analyzer.tokenStream("text", text);
            CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String word = StringUtils.removeAccentuation(term.toString());
                if (!DIGITS_PATTERN.matcher(word).matches()) {
                    words.add(word);
                }
            }
            tokenStream.end();
        } catch (Exception ex) {
            log.error("There was an error normalizing text", ex);
        } finally {
            try {
                if (tokenStream != null) {
                    tokenStream.close();
                }
            } catch (Exception ex) {
                log.error("There was an error trying to close token stream", ex);
            } finally {
                if (analyzer != null) {
                    analyzer.close();
                }
            }
        }
        return words;
    }

}

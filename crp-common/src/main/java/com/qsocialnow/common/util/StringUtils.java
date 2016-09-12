package com.qsocialnow.common.util;

import java.util.HashSet;
import java.util.Set;

public class StringUtils {

    private static final java.text.Normalizer.Form decomposed = java.text.Normalizer.Form.NFD;

    public static Set<String> convert2LowerCase(String[] toFold) {
        Set<String> foldedArray = new HashSet<String>();

        for (String fold : toFold) {
            fold = fold.toLowerCase();
            foldedArray.add(fold);
        }

        return foldedArray;
    }

    public static Set<String> convertLowerCaseAsciiFolding(String[] toFold) {
        Set<String> foldedArray = new HashSet<String>();

        for (String fold : toFold) {
            String palabra = removeAccentuation(fold);
            palabra = palabra.toLowerCase();
            foldedArray.add(palabra);
        }

        return foldedArray;
    }

    public static String removeAccentuation(String s) {
        /**
         * Remove accents (but keep tilde in ñ, Ñ and diaeresis in ü, Ü)
         * 
         * @param s
         *            a string.
         * @return The string without diacritics (keeps only tilde in ñ, Ñ and
         *         diaeresis in ü, Ü)
         */
        String res = java.text.Normalizer.normalize(s, decomposed);
        return res.replaceAll("\u006E\u0303", "ñ").replaceAll("\u004E\u0303", "Ñ").replaceAll("\u0075\u0308", "ü")
                .replaceAll("\u0055\u0308", "Ü").replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}

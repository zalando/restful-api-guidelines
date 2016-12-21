package de.zalando.zally.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WordUtil {
    private static final Inflector INFLECTOR = new Inflector();
    private static final Set<String> PLURAL_WHITELIST = new HashSet<>(Arrays.asList("vat"));

    public static boolean isPlural(String word) {
        if (PLURAL_WHITELIST.contains(word)) {
            return true;
        }
        String singular = INFLECTOR.singularize(word);
        String plural = INFLECTOR.pluralize(word);
        if (singular == null) System.out.println("singular null for " + word);
        if (plural == null) System.out.println("plural null for " + word);
        return plural.equals(word) && !singular.equals(word);
    }
}

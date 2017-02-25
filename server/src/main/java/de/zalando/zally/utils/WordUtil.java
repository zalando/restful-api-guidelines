package de.zalando.zally.utils;

import de.zalando.zally.external.jbossdna.Inflector;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WordUtil {
    private static final Inflector INFLECTOR = new Inflector();
    private static final Set<String> PLURAL_WHITELIST = new HashSet<>(Collections.singletonList("vat"));

    public static boolean isPlural(String word) {
        if (PLURAL_WHITELIST.contains(word)) {
            return true;
        }
        String singular = INFLECTOR.singularize(word);
        String plural = INFLECTOR.pluralize(word);
        return plural.equals(word) && !singular.equals(word);
    }
}

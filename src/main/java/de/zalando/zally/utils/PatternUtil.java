package de.zalando.zally.utils;

import java.util.Arrays;

/**
 * Utility library for matching common patterns
 */
public class PatternUtil {

    private static final String LOWER_CASE_HYPHENS_PATTERN = "^[a-z-]*$";
    private static final String CAMEL_CASE_PATTERN = "^[a-z]+(?:[A-Z][a-z]+)*$";
    private static final String PASCAL_CASE_PATTERN = "^[A-Z][a-z]+(?:[A-Z][a-z]+)*$";
    private static final String HYPHENATED_CAMEL_CASE_PATTERN = "^[a-z]+(?:-[A-Z][a-z]+)*$";
    private static final String HYPHENATED_PASCAL_CASE_PATTERN = "^[A-Z][a-z]+(?:-[A-Z][a-z]+)*$";
    private static final String SNAKE_CASE_PATTERN = "^[a-z]+(?:_[a-z0-9]+)*$";
    private static final String KEBAB_CASE_PATTERN = "^[a-z]+(?:-[a-z]+)*$";
    private static final String VERSION_IN_URL_PATTERN = "(.*)/v[0-9]+(.*)";
    private static final String PATH_VARIABLE_PATTERN = "\\{.+\\}$";
    private static final String GENERIC_VERSION_PATTERN = "^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$";

    public static boolean hasTrailingSlash(String input) {
        return input.trim().endsWith("/");
    }

    public static boolean isLowerCaseAndHyphens(String input) {
        return input.matches(LOWER_CASE_HYPHENS_PATTERN);
    }

    public static boolean isPathVariable(String input) {
        return input.matches(PATH_VARIABLE_PATTERN);
    }

    public static boolean isCamelCase(String input) {
        return input.matches(CAMEL_CASE_PATTERN);
    }

    public static boolean isPascalCase(String input) {
        return input.matches(PASCAL_CASE_PATTERN);
    }

    public static boolean isHyphenatedCamelCase(String input) {
        return input.matches(HYPHENATED_CAMEL_CASE_PATTERN);
    }

    public static boolean isHyphenatedPascalCase(String input) {
        return input.matches(HYPHENATED_PASCAL_CASE_PATTERN);
    }

    public static boolean isSnakeCase(String input) {
        return input.matches(SNAKE_CASE_PATTERN);
    }

    public static boolean isKebabCase(String input) {
        return input.matches(KEBAB_CASE_PATTERN);
    }

    public static boolean isHyphenated(String input) {
        return Arrays.stream(input.split("-")).allMatch(p -> p.matches("([A-Z][^A-Z ]*)|([^A-Z ]+)"));
    }

    public static boolean hasVersionInUrl(String input) {
        return input.matches(VERSION_IN_URL_PATTERN);
    }

    public static boolean isVersion(String input) {
        return input.matches(GENERIC_VERSION_PATTERN);
    }
}

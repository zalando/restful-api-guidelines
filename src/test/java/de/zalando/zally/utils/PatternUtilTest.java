package de.zalando.zally.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for patterns utility
 */
public class PatternUtilTest {

    @Test
    public void checkHasTrailingSlash() {
        assertTrue(PatternUtil.hasTrailingSlash("blah/"));
        assertFalse(PatternUtil.hasTrailingSlash("blah"));
    }

    @Test
    public void checkIsLowerCaseAndHyphens() {
        assertTrue(PatternUtil.isLowerCaseAndHyphens("a-b-c"));
        assertTrue(PatternUtil.isLowerCaseAndHyphens("abc"));
        assertFalse(PatternUtil.isLowerCaseAndHyphens("A-B-C"));
    }

    @Test
    public void checkIsPathVariable() {
        assertTrue(PatternUtil.isPathVariable("{test}"));
        assertFalse(PatternUtil.isPathVariable("{}"));
        assertFalse(PatternUtil.isPathVariable(" { } "));
        assertFalse(PatternUtil.isPathVariable("abc"));
        assertFalse(PatternUtil.isPathVariable("{test"));
        assertFalse(PatternUtil.isPathVariable("test}"));
    }

    @Test
    public void checkIsCamelCase() {
        assertTrue(PatternUtil.isCamelCase("testCase"));
        assertFalse(PatternUtil.isCamelCase("TestCase"));
    }

    @Test
    public void checkIsPascalCase() {
        assertTrue(PatternUtil.isPascalCase("TestCase"));
        assertFalse(PatternUtil.isPascalCase("testCase"));
    }

    @Test
    public void checkIsHyphenatedCamelCase() {
        assertTrue(PatternUtil.isHyphenatedCamelCase("test-Case"));
        assertFalse(PatternUtil.isHyphenatedCamelCase("Test-Case"));
        assertFalse(PatternUtil.isHyphenatedCamelCase("testCase"));
        assertFalse(PatternUtil.isHyphenatedCamelCase("TestCase"));
    }

    @Test
    public void checkIsHyphenatedPascalCase() {
        assertTrue(PatternUtil.isHyphenatedPascalCase("Test-Case"));
        assertFalse(PatternUtil.isHyphenatedPascalCase("test-Case"));
        assertFalse(PatternUtil.isHyphenatedPascalCase("TestCase"));
        assertFalse(PatternUtil.isHyphenatedPascalCase("testCase"));
    }

    @Test
    public void checkIsSnakeCase() {
        assertTrue(PatternUtil.isSnakeCase("test_case"));
        assertTrue(PatternUtil.isSnakeCase("test"));
        assertFalse(PatternUtil.isSnakeCase("TestCase"));
        assertFalse(PatternUtil.isSnakeCase("Test_Case"));
        assertFalse(PatternUtil.isSnakeCase(""));
        assertFalse(PatternUtil.isSnakeCase("_"));
        assertFalse(PatternUtil.isSnakeCase("customer-number"));
        assertFalse(PatternUtil.isSnakeCase("_customer_number"));
        assertFalse(PatternUtil.isSnakeCase("CUSTOMER_NUMBER"));
    }

    @Test
    public void checkIsKebabCase() {
        assertTrue(PatternUtil.isKebabCase("test-case"));
        assertFalse(PatternUtil.isKebabCase("test-Case"));
        assertFalse(PatternUtil.isKebabCase("testCase"));
    }

    @Test
    public void checkIsHyphenated() {
        assertTrue(PatternUtil.isHyphenated("A"));
        assertTrue(PatternUtil.isHyphenated("low"));
        assertTrue(PatternUtil.isHyphenated("Aa"));
        assertFalse(PatternUtil.isHyphenated("aA"));
        assertFalse(PatternUtil.isHyphenated("AA"));
        assertTrue(PatternUtil.isHyphenated("A-A"));
        assertTrue(PatternUtil.isHyphenated("X-Auth-2.0"));
        assertTrue(PatternUtil.isHyphenated("This-Is-Some-Hyphenated-String"));
        assertTrue(PatternUtil.isHyphenated("this-is-other-hyphenated-string"));
        assertFalse(PatternUtil.isHyphenated("Sorry no hyphens here"));
        assertFalse(PatternUtil.isHyphenated("CamelCaseIsNotAcceptableAndShouldBeIllegal"));
        assertFalse(PatternUtil.isHyphenated("a--a"));
    }

    @Test
    public void checkHasVersionInUrl() {
        assertTrue(PatternUtil.hasVersionInUrl("path/to/v1"));
        assertTrue(PatternUtil.hasVersionInUrl("path/to/v1/"));
        assertFalse(PatternUtil.hasVersionInUrl("path/to"));
    }
}

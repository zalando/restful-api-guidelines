package de.zalando.zally.utils;

import org.junit.Test;

import static de.zalando.zally.utils.PatternUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for patterns utility
 */
public class PatternUtilTest {

    @Test
    public void checkHasTrailingSlash() {
        assertTrue(hasTrailingSlash("blah/"));
        assertFalse(hasTrailingSlash("blah"));
    }

    @Test
    public void checkIsLowerCaseAndHyphens() {
        assertTrue(isLowerCaseAndHyphens("a-b-c"));
        assertTrue(isLowerCaseAndHyphens("abc"));
        assertFalse(isLowerCaseAndHyphens("A-B-C"));
    }

    @Test
    public void checkIsPathVariable() {
        assertTrue(isPathVariable("{test}"));
        assertFalse(isPathVariable("{}"));
        assertFalse(isPathVariable(" { } "));
        assertFalse(isPathVariable("abc"));
        assertFalse(isPathVariable("{test"));
        assertFalse(isPathVariable("test}"));
    }

    @Test
    public void checkIsCamelCase() {
        assertTrue(isCamelCase("testCase"));
        assertFalse(isCamelCase("TestCase"));
    }

    @Test
    public void checkIsPascalCase() {
        assertTrue(isPascalCase("TestCase"));
        assertFalse(isPascalCase("testCase"));
    }

    @Test
    public void checkIsHyphenatedCamelCase() {
        assertTrue(isHyphenatedCamelCase("test-Case"));
        assertFalse(isHyphenatedCamelCase("Test-Case"));
        assertFalse(isHyphenatedCamelCase("testCase"));
        assertFalse(isHyphenatedCamelCase("TestCase"));
    }

    @Test
    public void checkIsHyphenatedPascalCase() {
        assertTrue(isHyphenatedPascalCase("Test-Case"));
        assertFalse(isHyphenatedPascalCase("test-Case"));
        assertFalse(isHyphenatedPascalCase("TestCase"));
        assertFalse(isHyphenatedPascalCase("testCase"));
    }

    @Test
    public void checkIsSnakeCase() {
        assertTrue(isSnakeCase("test_case"));
        assertTrue(isSnakeCase("test"));
        assertFalse(isSnakeCase("TestCase"));
        assertFalse(isSnakeCase("Test_Case"));
        assertFalse(isSnakeCase(""));
        assertFalse(isSnakeCase("_"));
        assertFalse(isSnakeCase("customer-number"));
        assertFalse(isSnakeCase("_customer_number"));
        assertFalse(isSnakeCase("CUSTOMER_NUMBER"));
    }

    @Test
    public void checkIsKebabCase() {
        assertTrue(isKebabCase("test-case"));
        assertFalse(isKebabCase("test-Case"));
        assertFalse(isKebabCase("testCase"));
    }

    @Test
    public void checkIsHyphenated() {
        assertTrue(isHyphenated("A"));
        assertTrue(isHyphenated("low"));
        assertTrue(isHyphenated("Aa"));
        assertFalse(isHyphenated("aA"));
        assertFalse(isHyphenated("AA"));
        assertTrue(isHyphenated("A-A"));
        assertTrue(isHyphenated("X-Auth-2.0"));
        assertTrue(isHyphenated("This-Is-Some-Hyphenated-String"));
        assertTrue(isHyphenated("this-is-other-hyphenated-string"));
        assertFalse(isHyphenated("Sorry no hyphens here"));
        assertFalse(isHyphenated("CamelCaseIsNotAcceptableAndShouldBeIllegal"));
        assertFalse(isHyphenated("a--a"));
    }

    @Test
    public void checkHasVersionInUrl() {
        assertTrue(hasVersionInUrl("path/to/v1"));
        assertTrue(hasVersionInUrl("path/to/v1/"));
        assertFalse(hasVersionInUrl("path/to"));
    }

    @Test
    public void checkGenericIsVersion() {
        assertFalse(isVersion("*"));
        assertFalse(isVersion("1"));
        assertFalse(isVersion("1.2"));
        assertFalse(isVersion("12.3"));
        assertTrue(isVersion("1.2.3"));
        assertFalse(isVersion("1.23"));
        assertTrue(isVersion("1.2.34"));
        assertTrue(isVersion("123.456.789"));
        assertFalse(isVersion("1.2.*"));
        assertFalse(isVersion("1.*"));
        assertFalse(isVersion("a"));
        assertFalse(isVersion("1.a"));
        assertFalse(isVersion("*.1"));
        assertFalse(isVersion("1.*.2"));
    }
}

package com.dove.common.core.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for StringUtils
 */
class StringUtilsTest {

    @Test
    void testIsAlphanumeric() {
        assertTrue(StringUtils.isAlphanumeric("abc123"));
        assertTrue(StringUtils.isAlphanumeric("ABC123"));
        assertFalse(StringUtils.isAlphanumeric("abc 123"));
        assertFalse(StringUtils.isAlphanumeric("abc-123"));
        assertFalse(StringUtils.isAlphanumeric(""));
        assertFalse(StringUtils.isAlphanumeric(null));
    }

    @Test
    void testMask() {
        assertEquals("12***67", StringUtils.mask("1234567", 2, 5, '*'));
        assertEquals("*****67", StringUtils.mask("1234567", 0, 5, '*'));
        assertEquals("12****", StringUtils.mask("123456", 2, 10, '*'));
        assertEquals("123456", StringUtils.mask("123456", 6, 10, '*'));
        assertEquals("123456", StringUtils.mask("123456", -1, 0, '*'));
        assertEquals("", StringUtils.mask("", 0, 5, '*'));
        assertNull(StringUtils.mask(null, 0, 5, '*'));
    }
} 
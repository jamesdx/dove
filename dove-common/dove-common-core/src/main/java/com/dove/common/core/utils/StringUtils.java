package com.dove.common.core.utils;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import lombok.experimental.UtilityClass;

/**
 * String utility class that extends Apache Commons Lang StringUtils
 */
@UtilityClass
public class StringUtils {
    
    /**
     * Check if a string contains only letters and numbers
     *
     * @param str the string to check
     * @return true if the string contains only letters and numbers
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("^[a-zA-Z0-9]+$");
    }
    
    /**
     * Mask sensitive information in a string
     *
     * @param str the string to mask
     * @param start start position (inclusive)
     * @param end end position (exclusive)
     * @param maskChar the character to use for masking
     * @return the masked string
     */
    public static String mask(String str, int start, int end, char maskChar) {
        if (isEmpty(str)) {
            return str;
        }
        
        if (start < 0) {
            start = 0;
        }
        
        if (end > str.length()) {
            end = str.length();
        }
        
        if (start >= end) {
            return str;
        }
        
        StringBuilder masked = new StringBuilder(str);
        for (int i = start; i < end; i++) {
            masked.setCharAt(i, maskChar);
        }
        
        return masked.toString();
    }
} 
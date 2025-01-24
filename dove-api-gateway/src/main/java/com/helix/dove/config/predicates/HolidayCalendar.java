package com.helix.dove.config.predicates;

import java.time.LocalDate;

/**
 * Interface for checking holiday dates.
 */
public interface HolidayCalendar {
    /**
     * Check if a given date is a holiday.
     *
     * @param date the date to check
     * @return true if the date is a holiday, false otherwise
     */
    boolean isHoliday(LocalDate date);
} 
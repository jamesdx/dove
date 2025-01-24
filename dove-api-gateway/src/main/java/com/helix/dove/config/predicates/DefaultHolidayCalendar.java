package com.helix.dove.config.predicates;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation of HolidayCalendar that includes common US holidays.
 */
@Component
public class DefaultHolidayCalendar implements HolidayCalendar {

    private final Set<MonthDay> fixedHolidays;

    public DefaultHolidayCalendar() {
        fixedHolidays = new HashSet<>();
        // New Year's Day
        fixedHolidays.add(MonthDay.of(1, 1));
        // Independence Day
        fixedHolidays.add(MonthDay.of(7, 4));
        // Christmas Day
        fixedHolidays.add(MonthDay.of(12, 25));
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        MonthDay monthDay = MonthDay.from(date);
        return fixedHolidays.contains(monthDay);
    }
} 
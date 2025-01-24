package com.helix.dove.config.predicates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.helix.dove.config.predicates.TimeBasedRoutePredicateFactory;
import com.helix.dove.config.predicates.HolidayCalendar;

@ExtendWith(MockitoExtension.class)
@DisplayName("Time Based Route Predicate Factory Tests")
class TimeBasedRoutePredicateFactoryTest {

    private TimeBasedRoutePredicateFactory predicateFactory;
    
    @Mock
    private Clock clock;
    
    @Mock
    private HolidayCalendar holidayCalendar;

    @BeforeEach
    void setUp() {
        predicateFactory = new TimeBasedRoutePredicateFactory();
        predicateFactory.setClock(clock);
        lenient().when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Nested
    @DisplayName("Time Window Tests")
    class TimeWindowTests {

        @Test
        @DisplayName("Should allow request during business hours")
        void shouldAllowRequestDuringBusinessHours() {
            // Arrange
            TimeBasedRoutePredicateFactory.Config config = new TimeBasedRoutePredicateFactory.Config();
            config.setStartTime("09:00");
            config.setEndTime("17:00");
            config.setTimeZone("UTC");
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            when(clock.instant()).thenReturn(Instant.parse("2024-01-24T14:00:00Z"));
            when(clock.withZone(any(ZoneId.class))).thenReturn(clock);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }

        @Test
        @DisplayName("Should reject request outside business hours")
        void shouldRejectRequestOutsideBusinessHours() {
            // Arrange
            TimeBasedRoutePredicateFactory.Config config = new TimeBasedRoutePredicateFactory.Config();
            config.setStartTime("09:00");
            config.setEndTime("17:00");
            config.setTimeZone("UTC");
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            when(clock.instant()).thenReturn(Instant.parse("2024-01-24T20:00:00Z"));
            when(clock.withZone(any(ZoneId.class))).thenReturn(clock);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }
    }

    @Nested
    @DisplayName("Time Zone Tests")
    class TimeZoneTests {

        @Test
        @DisplayName("Should handle different time zones correctly")
        void shouldHandleDifferentTimeZonesCorrectly() {
            // Arrange
            TimeBasedRoutePredicateFactory.Config config = new TimeBasedRoutePredicateFactory.Config();
            config.setStartTime("09:00");
            config.setEndTime("17:00");
            config.setTimeZone("America/New_York");
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            // 14:00 UTC = 09:00 EST
            when(clock.instant()).thenReturn(Instant.parse("2024-01-24T14:00:00Z"));
            when(clock.withZone(any(ZoneId.class))).thenReturn(clock);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }

    @Nested
    @DisplayName("Holiday Calendar Tests")
    class HolidayCalendarTests {

        @Test
        @DisplayName("Should reject request on holidays")
        void shouldRejectRequestOnHolidays() {
            // Arrange
            TimeBasedRoutePredicateFactory.Config config = new TimeBasedRoutePredicateFactory.Config();
            config.setStartTime("09:00");
            config.setEndTime("17:00");
            config.setTimeZone("UTC");
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            // New Year's Day
            when(clock.instant()).thenReturn(Instant.parse("2024-01-01T14:00:00Z"));
            when(clock.withZone(any(ZoneId.class))).thenReturn(clock);
            
            predicateFactory.setHolidayCalendar(holidayCalendar);
            when(holidayCalendar.isHoliday(any(LocalDate.class))).thenReturn(true);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle midnight correctly")
        void shouldHandleMidnightCorrectly() {
            // Arrange
            TimeBasedRoutePredicateFactory.Config config = new TimeBasedRoutePredicateFactory.Config();
            config.setStartTime("00:00");
            config.setEndTime("24:00");
            config.setTimeZone("UTC");
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            when(clock.instant()).thenReturn(Instant.parse("2024-01-24T00:00:00Z"));
            when(clock.withZone(any(ZoneId.class))).thenReturn(clock);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }

        @Test
        @DisplayName("Should handle DST changes correctly")
        void shouldHandleDSTChangesCorrectly() {
            // Arrange
            TimeBasedRoutePredicateFactory.Config config = new TimeBasedRoutePredicateFactory.Config();
            config.setStartTime("09:00");
            config.setEndTime("17:00");
            config.setTimeZone("America/New_York");
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            // During DST
            when(clock.instant()).thenReturn(Instant.parse("2024-03-10T14:00:00Z"));
            when(clock.withZone(any(ZoneId.class))).thenReturn(clock);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should reject invalid time format")
        void shouldRejectInvalidTimeFormat() {
            // Arrange
            TimeBasedRoutePredicateFactory.Config config = new TimeBasedRoutePredicateFactory.Config();
            config.setStartTime("25:00"); // Invalid hour
            config.setEndTime("17:00");
            config.setTimeZone("UTC");
            
            // Act & Assert
            assertThatThrownBy(() -> predicateFactory.apply(config))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid time format");
        }

        @Test
        @DisplayName("Should reject invalid timezone")
        void shouldRejectInvalidTimezone() {
            // Arrange
            TimeBasedRoutePredicateFactory.Config config = new TimeBasedRoutePredicateFactory.Config();
            config.setStartTime("09:00");
            config.setEndTime("17:00");
            config.setTimeZone("Invalid/Timezone");
            
            // Act & Assert
            assertThatThrownBy(() -> predicateFactory.apply(config))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid time zone");
        }
    }
} 
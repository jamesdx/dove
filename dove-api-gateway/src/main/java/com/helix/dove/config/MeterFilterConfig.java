package com.helix.dove.config;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;

public class MeterFilterConfig {

    public static MeterFilter denyNameStartsWith(String prefix) {
        return new MeterFilter() {
            @Override
            public MeterFilterReply accept(Meter.Id id) {
                return id.getName().startsWith(prefix) ? MeterFilterReply.DENY : MeterFilterReply.NEUTRAL;
            }
        };
    }

    public static MeterFilter maximumAllowableMetrics(int maximumNumber) {
        return new MeterFilter() {
            private int count = 0;

            @Override
            public synchronized MeterFilterReply accept(Meter.Id id) {
                if (count >= maximumNumber) {
                    return MeterFilterReply.DENY;
                }
                count++;
                return MeterFilterReply.NEUTRAL;
            }
        };
    }
} 
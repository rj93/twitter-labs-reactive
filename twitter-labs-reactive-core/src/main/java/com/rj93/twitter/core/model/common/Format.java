package com.rj93.twitter.core.model.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rj93.twitter.core.query.Expansion;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public enum Format {
    DEFAULT("default"),
    COMPACT("compact"),
    DETAILED("detailed");
    private static final Map<String, Format> CONSTANTS = new HashMap();

    static {
        Format[] values = values();
        for (int i = 0; i < values.length; ++i) {
            Format value = values[i];
            CONSTANTS.put(value.value, value);
        }
    }

    private String value;

    Format(String str) {
        value = str;
    }

    @JsonCreator
    public static Format fromValue(String value) {
        Format constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

    public String getValue() {
        return this.value;
    }
}

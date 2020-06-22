package com.rj93.twitter.core.model.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;

public enum Format {
    DEFAULT("default"),
    COMPACT("compact"),
    DETAILED("detailed");
    private static final Map<String, Format> CONSTANTS = new HashMap();

    static {
        Format[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            Format c = var0[var2];
            CONSTANTS.put(c.value, c);
        }

    }

    private String value;

    Format(String str) {
        value = str;
    }

    @JsonCreator
    public static Format fromValue(String value) {
        Format constant = (Format) CONSTANTS.get(value);
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

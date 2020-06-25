package com.rj93.twitter.core.model.tweet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rj93.twitter.core.query.Expansion;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Coordinates {
    private Type type;
    private List<Double> coordinates;

    public enum Type {
        POINT("Point");
        private static final Map<String, Type> CONSTANTS = new HashMap();

        static {
            Type[] values = values();
            for (int i = 0; i < values.length; ++i) {
                Type value = values[i];
                CONSTANTS.put(value.value, value);
            }
        }

        private String value;

        Type(String str) {
            value = str;
        }

        @JsonCreator
        public static Type fromValue(String value) {
            Type constant = CONSTANTS.get(value);
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
}

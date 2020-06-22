package com.rj93.twitter.core.model.tweet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
            Type[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                Type c = var0[var2];
                CONSTANTS.put(c.value, c);
            }

        }

        private String value;

        Type(String str) {
            value = str;
        }

        @JsonCreator
        public static Type fromValue(String value) {
            Type constant = (Type) CONSTANTS.get(value);
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

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
public class Withheld {
    private boolean copyright;
    private List<String> countryCodes;
    private Scope scope;

    public enum Scope {
        TWEET("tweet"),
        USER("user");
        private static final Map<String, Scope> CONSTANTS = new HashMap();

        static {
            Scope[] values = values();
            for (int i = 0; i < values.length; ++i) {
                Scope value = values[i];
                CONSTANTS.put(value.value, value);
            }
        }

        private String value;

        Scope(String str) {
            value = str;
        }

        @JsonCreator
        public static Scope fromValue(String value) {
            Scope constant = CONSTANTS.get(value);
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

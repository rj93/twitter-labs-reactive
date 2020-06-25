package com.rj93.twitter.core.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;

public enum Expansion {
    ATTACHMENTS_POLL_IDS("attachments.poll_ids"),
    ATTACHMENTS_MEDIA_KEYS("attachments.media_keys"),
    AUTHOR_ID("author_id"),
    ENTITIES_MENTIONS_USERNAME("entities.mentions.username"),
    GEO_PLACE_ID("geo.place_id"),
    IN_REPLY_TO_USER_ID("in_reply_to_user_id"),
    REFERENCE_TWEETS_ID("referenced_tweets.id"),
    REFERENCE_TWEETS_ID_AUTHOR_ID("referenced_tweets.id.author_id");

    private static final Map<String, Expansion> CONSTANTS = new HashMap();

    static {
        Expansion[] values = values();
        for (int i = 0; i < values.length; ++i) {
            Expansion value = values[i];
            CONSTANTS.put(value.value, value);
        }
    }

    private String value;

    Expansion(String str) {
        value = str;
    }

    @JsonCreator
    public static Expansion fromValue(String value) {
        Expansion constant = CONSTANTS.get(value);
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

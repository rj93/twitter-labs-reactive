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
        Expansion[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            Expansion c = var0[var2];
            CONSTANTS.put(c.value, c);
        }

    }

    private String value;

    Expansion(String str) {
        value = str;
    }

    @JsonCreator
    public static Expansion fromValue(String value) {
        Expansion constant = (Expansion) CONSTANTS.get(value);
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

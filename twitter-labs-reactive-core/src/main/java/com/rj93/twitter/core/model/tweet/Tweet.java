package com.rj93.twitter.core.model.tweet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rj93.twitter.core.model.common.Format;
import com.rj93.twitter.core.model.tweet.entity.Entities;
import java.util.Date;
import java.util.List;
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
public class Tweet {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createdAt;
    private String text;
    private String authorId;
    private Geo geo;
    private String inReplyToUserId;
    private List<ReferencedTweet> referencedTweets;
    private Attachments attachments;
    private Entities entities;
    private Stats stats;
    private boolean possiblySensitive;
    private String lang;
    private String source;
    private List<ContextAnnotation> contextAnnotations;
    private Format format;
    private Withheld withheld;
}

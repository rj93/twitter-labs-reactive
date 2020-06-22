package com.rj93.twitter.core.model.tweet.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class Entities {
    private List<Url> urls;
    private List<Annotation> annotations;
    private List<Hashtag> hashtags;
    private List<Mention> mentions;
    private List<Cashtag> cashtags;
}

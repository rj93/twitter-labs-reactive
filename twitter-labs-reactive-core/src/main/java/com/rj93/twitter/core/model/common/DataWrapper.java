package com.rj93.twitter.core.model.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DataWrapper<T> {
    private T data;
    private Meta meta;
    private List<Rule> matchingRules;
    private List<Error> errors;
}

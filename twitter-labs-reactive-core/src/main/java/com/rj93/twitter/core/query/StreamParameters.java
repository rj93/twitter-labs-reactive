package com.rj93.twitter.core.query;

import com.rj93.twitter.core.model.common.Format;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StreamParameters {
    private List<Expansion> expansions;
    private Format format;
    private Format placeFormat;
    private Format tweetFormat;
    private Format userFormat;
}

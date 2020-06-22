package com.rj93.twitter.core.model.rule;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Delete {
    private List<String> ids;
    private List<String> values;
}

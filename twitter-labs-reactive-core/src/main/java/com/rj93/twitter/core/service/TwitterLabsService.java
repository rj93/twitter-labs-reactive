package com.rj93.twitter.core.service;

import com.rj93.twitter.core.model.common.DataWrapper;
import com.rj93.twitter.core.model.common.Rule;
import com.rj93.twitter.core.model.tweet.Tweet;
import com.rj93.twitter.core.query.StreamParameters;
import java.util.Collections;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TwitterLabsService {

    default Flux<Tweet> sample() {
        return sample(new StreamParameters());
    }

    Flux<Tweet> sample(StreamParameters streamParameters);

    default Flux<Tweet> filter() {
        return filter(new StreamParameters());
    }

    Flux<Tweet> filter(StreamParameters streamParameters);

    default Mono<Rule> getRule(String id) {
        return getRules(Collections.singletonList(id))
                .map(DataWrapper::getData)
                .map(rules -> rules.get(0));
    }
    default Mono<DataWrapper<List<Rule>>> getAllRules() {
        return getRules(Collections.emptyList());
    }

    Mono<DataWrapper<List<Rule>>> getRules(List<String> ids);

    default Mono<DataWrapper<List<Rule>>> addRule(Rule rule) {
        return addRule(Collections.singletonList(rule));
    }

    Mono<DataWrapper<List<Rule>>> addRule(List<Rule> rules);

    default Mono<DataWrapper<Void>> deleteRules(String id) {
        return deleteRules(Collections.singletonList(id));
    }
    Mono<DataWrapper<Void>> deleteRules(List<String> ids);

    Mono<DataWrapper<Void>> deleteAllRules();

}

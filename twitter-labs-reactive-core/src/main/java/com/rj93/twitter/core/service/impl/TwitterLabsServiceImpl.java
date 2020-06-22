package com.rj93.twitter.core.service.impl;

import com.rj93.twitter.core.model.common.DataWrapper;
import com.rj93.twitter.core.model.common.Format;
import com.rj93.twitter.core.model.common.Rule;
import com.rj93.twitter.core.model.rule.AddWrapper;
import com.rj93.twitter.core.model.rule.Delete;
import com.rj93.twitter.core.model.rule.DeleteWrapper;
import com.rj93.twitter.core.model.tweet.Tweet;
import com.rj93.twitter.core.query.Expansion;
import com.rj93.twitter.core.query.StreamParameters;
import com.rj93.twitter.core.service.TwitterLabsService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class TwitterLabsServiceImpl implements TwitterLabsService {

    private static final String SAMPLE_PATH = "/1/tweets/stream/sample";
    private static final String FILTER_PATH = "/1/tweets/stream/filter";
    private static final String FILTER_RULES_PATH = "/1/tweets/stream/filter/rules";

    private static final String EXPANSIONS_QUERY_PARAM = "expansions";
    private static final String FORMAT_QUERY_PARAM = "format";
    private static final String PLACE_FORMAT_QUERY_PARAM = "place.format";
    private static final String TWEET_FORMAT_QUERY_PARAM = "tweet.format";
    private static final String USER_FORMAT_QUERY_PARAM = "user.format";

    private static final String IDS_QUERY_PARAM = "ids";

    private final WebClient webClient;

    public TwitterLabsServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Tweet> sample(StreamParameters streamParameters) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, SAMPLE_PATH, streamParameters))
                .exchange()
                .flatMapMany(this::handleResponse);
    }

    @Override
    public Flux<Tweet> filter(StreamParameters streamParameters) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, FILTER_PATH, streamParameters))
                .exchange()
                .flatMapMany(this::handleResponse);
    }

    @Override
    public Mono<DataWrapper<List<Rule>>> getRules(List<String> ids) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(FILTER_RULES_PATH);
                    if (!ids.isEmpty()) {
                        uriBuilder.queryParam(IDS_QUERY_PARAM, ids.stream().collect(stringCollector()));
                    }
                    return uriBuilder.build();
                })
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(new ParameterizedTypeReference<DataWrapper<List<Rule>>>() {
                }));
    }

    @Override
    public Mono<DataWrapper<List<Rule>>> addRule(List<Rule> rules) {
        AddWrapper addWrapper = AddWrapper.builder().rules(rules).build();
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(FILTER_RULES_PATH).build())
                .body(BodyInserters.fromValue(addWrapper))
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(new ParameterizedTypeReference<DataWrapper<List<Rule>>>() {}));
    }

    @Override
    public Mono<DataWrapper<Void>> deleteRules(List<String> ids) {
        DeleteWrapper deleteWrapper = DeleteWrapper.builder()
                .delete(Delete.builder().ids(ids).build())
                .build();

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(FILTER_RULES_PATH).build())
                .body(BodyInserters.fromValue(deleteWrapper))
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(new ParameterizedTypeReference<DataWrapper<Void>>() {}));
    }

    @Override
    public Mono<DataWrapper<Void>> deleteAllRules() {
        return getAllRules()
                .map(DataWrapper::getData)
                .flatMapMany(Flux::fromIterable)
                .map(Rule::getId)
                .collectList()
                .flatMap(this::deleteRules);
    }

    private URI buildUri(UriBuilder uriBuilder, String path, StreamParameters streamParameters) {
        uriBuilder.path(path);

        if (streamParameters.getExpansions() != null && !streamParameters.getExpansions().isEmpty()) {
            uriBuilder.queryParam(EXPANSIONS_QUERY_PARAM, streamParameters.getExpansions()
                    .stream()
                    .map(Expansion::getValue)
                    .collect(stringCollector()));
        }

        appendFormat(uriBuilder, FORMAT_QUERY_PARAM, streamParameters.getFormat());
        appendFormat(uriBuilder, PLACE_FORMAT_QUERY_PARAM, streamParameters.getPlaceFormat());
        appendFormat(uriBuilder, TWEET_FORMAT_QUERY_PARAM, streamParameters.getTweetFormat());
        appendFormat(uriBuilder, USER_FORMAT_QUERY_PARAM, streamParameters.getUserFormat());

        return uriBuilder.build();
    }

    private void appendFormat(UriBuilder uriBuilder, String name, Format format) {
        if (format != null) {
            uriBuilder.queryParam(name, format.getValue());
        }
    }

    private Flux<Tweet> handleResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToFlux(new ParameterizedTypeReference<DataWrapper<Tweet>>() {
        })
                .map(DataWrapper::getData)
                .onErrorContinue((e, o) -> log.error("Failed to deserialize tweet : {}. object: {}", e.getMessage(), o, e));
    }

    private Collector<CharSequence, ?, String> stringCollector() {
        return Collectors.joining(",");
    }
}

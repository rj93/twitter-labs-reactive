package com.rj93.twitter.app.rest;

import com.rj93.twitter.core.model.common.DataWrapper;
import com.rj93.twitter.core.model.common.Rule;
import com.rj93.twitter.core.service.TwitterLabsService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rules")
@Slf4j
public class RuleController {

    private final TwitterLabsService twitterLabsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private Mono<List<Rule>> getAllRules() {
        log.info("getAllRules");
        return twitterLabsService.getAllRules()
                .map(DataWrapper::getData)
                .onErrorReturn(Collections.emptyList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private Mono<Rule> getRule(@PathVariable String id) {
        log.info("getRule: {}", id);
        return twitterLabsService.getRule(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Mono<DataWrapper<List<Rule>>> createRule(@RequestBody Rule rule) {
        log.info("createRule: {}", rule);
        return twitterLabsService.addRule(rule);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    private Mono<DataWrapper<Void>> deleteRule(@PathVariable String id) {
        log.info("deleteRule: {}", id);
        return twitterLabsService.deleteRules(id);
    }

    @DeleteMapping("/delete/all")
    @ResponseStatus(HttpStatus.ACCEPTED)
    private Mono<DataWrapper<Void>> deleteAllRules() {
        log.info("deleteAllRules");
        return twitterLabsService.deleteAllRules();
    }
}

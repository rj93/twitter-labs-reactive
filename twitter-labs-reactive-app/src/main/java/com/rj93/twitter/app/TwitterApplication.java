package com.rj93.twitter.app;

import com.rj93.twitter.core.model.common.Format;
import com.rj93.twitter.core.model.tweet.Tweet;
import com.rj93.twitter.core.query.StreamParameters;
import com.rj93.twitter.core.service.TwitterLabsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
public class TwitterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterApplication.class, args);
    }

    @Bean
    public CommandLineRunner tweetBot(TwitterLabsService twitterLabsService) {
        return args -> {
            StreamParameters streamParameters = StreamParameters.builder()
                    .format(Format.DETAILED)
                    .build();

            Flux<Tweet> sample = twitterLabsService.sample(streamParameters);
            Flux<Tweet> filter = twitterLabsService.filter(streamParameters);

            Flux.merge(sample, filter)
                    .log()
                    .subscribe();
        };
    }
}

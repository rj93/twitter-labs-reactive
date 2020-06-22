package com.rj93.twitter.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj93.twitter.core.service.TwitterLabsService;
import com.rj93.twitter.core.service.impl.TwitterLabsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:twitter-oauth2.properties")
@AutoConfigureAfter(ReactiveOAuth2ClientAutoConfiguration.class)
@ConditionalOnClass(TwitterLabsService.class)
@ConditionalOnBean(ReactiveClientRegistrationRepository.class)
@Slf4j
public class TwitterAutoConfiguration {

    private static final String TWITTER_CLIENT_REGISTRATION_ID = "twitter";

    @Bean
    TwitterLabsService tweetLabsService(@Qualifier("twitterLabsWebClient") WebClient webClient) {
        return new TwitterLabsServiceImpl(webClient);
    }

    @Bean(name = "twitterLabsWebClient")
    WebClient twitterLab(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("twitter").block();

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, new UnAuthenticatedServerOAuth2AuthorizedClientRepository());
        oauth2.setDefaultClientRegistrationId(TWITTER_CLIENT_REGISTRATION_ID);

        ObjectMapper objectMapper = new ObjectMapper();
        ExchangeStrategies strategies = ExchangeStrategies.builder().codecs(clientCodecConfigurer ->
                clientCodecConfigurer.customCodecs().registerWithDefaultConfig(
                        new Jackson2JsonDecoder(objectMapper, new MimeType("application", "octet-stream")))
        ).build();

        return WebClient.builder()
                .baseUrl("https://api.twitter.com/labs")
                .exchangeStrategies(strategies)
                .filter(oauth2)
                .build();
    }
}

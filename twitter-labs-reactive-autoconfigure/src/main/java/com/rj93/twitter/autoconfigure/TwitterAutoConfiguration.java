package com.rj93.twitter.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj93.twitter.core.service.TwitterLabsService;
import com.rj93.twitter.core.service.impl.TwitterLabsServiceImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:twitter-oauth2.properties")
@AutoConfigureAfter(ReactiveOAuth2ClientAutoConfiguration.class)
@ConditionalOnClass(TwitterLabsService.class)
@EnableConfigurationProperties(TwitterConfigurationProperties.class)
@Slf4j
public class TwitterAutoConfiguration {

    private static final String TWITTER_LABS_BASE_URL = "https://api.twitter.com/labs";

    @Bean
    TwitterLabsService tweetLabsService(@Qualifier("twitterLabsWebClient") WebClient webClient) {
        return new TwitterLabsServiceImpl(webClient);
    }

    @Bean(name = "twitterLabsWebClient")
    WebClient twitterLab(
            ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager,
            TwitterConfigurationProperties twitterConfigurationProperties) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(reactiveOAuth2AuthorizedClientManager);
        oauth2.setDefaultClientRegistrationId(twitterConfigurationProperties.getClientRegistrationId());

        Jackson2JsonDecoder decoder = new Jackson2JsonDecoder(new ObjectMapper(), MimeTypeUtils.APPLICATION_OCTET_STREAM);
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .customCodecs()
                        .registerWithDefaultConfig(decoder)
                )
                .build();

        return WebClient.builder()
                .baseUrl(TWITTER_LABS_BASE_URL)
                .exchangeStrategies(strategies)
                .filter(oauth2)
                .build();
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    @ConditionalOnMissingBean
    @Bean
    public ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService(
            ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        return new InMemoryReactiveOAuth2AuthorizedClientService(reactiveClientRegistrationRepository);
    }

    @ConditionalOnMissingBean
    @Bean
    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository(
            OAuth2ClientProperties oAuth2ClientProperties) {

        List<ClientRegistration> registrations = new ArrayList<>(
                OAuth2ClientPropertiesRegistrationAdapter
                        .getClientRegistrations(oAuth2ClientProperties)
                        .values()
        );
        return new InMemoryReactiveClientRegistrationRepository(registrations);
    }

}

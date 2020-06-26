package com.rj93.twitter.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("twitter.oauth2")
public class TwitterConfigurationProperties {
    private String clientRegistrationId = "twitter";
}

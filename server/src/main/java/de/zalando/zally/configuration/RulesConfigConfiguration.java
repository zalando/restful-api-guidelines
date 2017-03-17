package de.zalando.zally.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RulesConfigConfiguration {

    @Bean
    public Config createRulesConfig() {
        return ConfigFactory.load("rules-config.conf");
    }
}

package de.zalando.zally.configuration;

import de.zalando.zally.rule.ApiValidator;
import de.zalando.zally.rule.CompositeRulesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RulesValidatorConfiguration {

    @Autowired
    private CompositeRulesValidator compositeValidator;

    @Bean
    @Primary
    public ApiValidator validator() {
        return compositeValidator;
    }
}

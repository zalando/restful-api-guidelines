package de.zalando.zally.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.zalando.problem.ProblemModule;

@Configuration
public class JacksonObjectMapperConfiguration {

    @Bean
    @Primary
    public ObjectMapper createObjectMapper() {
        return configure(new ObjectMapper());
    }

    @Bean
    @Qualifier("yamlObjectMapper")
    public ObjectMapper createYamlObjectMapper() {
        return configure(new ObjectMapper(new YAMLFactory()));
    }

    private ObjectMapper configure(ObjectMapper mapper) {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .registerModules(
                        new Jdk8Module(),
                        new JavaTimeModule(),
                        new ProblemModule(),
                        new KotlinModule());
        return mapper;
    }
}

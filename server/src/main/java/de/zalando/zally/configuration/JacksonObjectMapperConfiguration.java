package de.zalando.zally.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;

@Configuration
public class JacksonObjectMapperConfiguration {

    @Bean
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper().registerModules(new Jdk8Module(), new ProblemModule());
    }
}

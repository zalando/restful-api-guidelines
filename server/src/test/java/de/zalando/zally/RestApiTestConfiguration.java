package de.zalando.zally;

import de.zalando.zally.rules.Rule;
import de.zalando.zally.rules.RulesValidator;
import io.swagger.models.Swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class RestApiTestConfiguration {
    private static class CheckApiNameIsPresentRule implements Rule {

        private final String apiName;

        public CheckApiNameIsPresentRule(String apiName) {
            this.apiName = apiName;
        }

        @Override
        public List<Violation> validate(Swagger swagger) {
            if (swagger != null && swagger.getInfo().getTitle().contains(apiName)) {
                return Arrays.asList(new Violation("dummy1", "dummy", ViolationType.MUST, "dummy"));
            } else {
                return Collections.emptyList();
            }
        }
    }

    @Bean
    @Primary
    public RulesValidator validator() {
        return new RulesValidator(Arrays.asList(new CheckApiNameIsPresentRule("Product Service")));
    }
}

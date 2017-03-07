package de.zalando.zally;

import java.util.Collections;

import de.zalando.zally.rules.AbstractRule;
import de.zalando.zally.rules.RulesValidator;
import io.swagger.models.Swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RestApiTestConfiguration {

    private static class CheckApiNameIsPresentRule extends AbstractRule {

        private final String apiName;

        CheckApiNameIsPresentRule(String apiName) {
            this.apiName = apiName;
        }

        @Override
        public Violation validate(Swagger swagger) {
            if (swagger != null && swagger.getInfo().getTitle().contains(apiName)) {
                return new Violation(new CheckApiNameIsPresentRule(null), "dummy1", "dummy", ViolationType.MUST, "dummy", Collections.emptyList());
            } else {
                return null;
            }
        }
    }

    @Bean
    @Primary
    public RulesValidator validator() {
        return new RulesValidator(Collections.singletonList(new CheckApiNameIsPresentRule("Product Service")));
    }
}

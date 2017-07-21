package de.zalando.zally.apireview;

import de.zalando.zally.dto.ViolationType;
import de.zalando.zally.rule.AbstractRule;
import de.zalando.zally.rule.Rule;
import de.zalando.zally.rule.RulesPolicy;
import de.zalando.zally.rule.RulesValidator;
import de.zalando.zally.rule.Violation;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class RestApiTestConfiguration {

    @Autowired
    private RulesPolicy rulesPolicy;

    @Bean
    @Primary
    public RulesValidator validator() {
        final List<Rule> rules = Arrays.asList(
            new CheckApiNameIsPresentRule("Product Service"),
            new AlwaysGiveAHintRule()
        );
        return new RulesValidator(rules, rulesPolicy);
    }

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

        @Override
        public String getUrl() {
            return null;
        }

        @Override
        public ViolationType getViolationType() {
            return ViolationType.MUST;
        }

        @Override
        public String getTitle() {
            return "Test Rule";
        }

        @Override
        public String getCode() {
            return "M999";
        }
    }

    private static class AlwaysGiveAHintRule extends AbstractRule {
        @Override
        public Violation validate(Swagger swagger) {
            return new Violation(
                new AlwaysGiveAHintRule(),
                "dummy2", "dummy", ViolationType.HINT, "dummy", Collections.emptyList());
        }

        @Override
        public String getUrl() {
            return null;
        }

        @Override
        public ViolationType getViolationType() {
            return ViolationType.MUST;
        }

        @Override
        public String getTitle() {
            return "Test Hint Rule";
        }

        @Override
        public String getCode() {
            return "H999";
        }
    }
}

package de.zalando.zally.apireview;

import de.zalando.zally.dto.ApiDefinitionRequest;
import de.zalando.zally.dto.ViolationType;
import de.zalando.zally.rule.Rule;
import de.zalando.zally.rule.Violation;
import de.zalando.zally.util.ResourceUtil;
import io.swagger.models.Swagger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiReviewTest {

    private Rule dummyRule = new Rule() {

        @NotNull
        @Override
        public String getTitle() {
            return null;
        }

        @NotNull
        @Override
        public ViolationType getViolationType() {
            return null;
        }

        @Nullable
        @Override
        public String getUrl() {
            return null;
        }

        @NotNull
        @Override
        public String getCode() {
            return null;
        }

        @NotNull
        @Override
        public String getName() {
            return "dummyRule";
        }

        @Nullable
        @Override
        public Violation validate(@NotNull Swagger swagger) {
            return null;
        }
    };

    @Test
    public void shouldAggregateRuleTypeCount() {
        Violation mustViolation1 = new Violation(dummyRule, "", "", ViolationType.MUST, "", Collections.emptyList());
        Violation mustViolation2 = new Violation(dummyRule, "", "", ViolationType.MUST, "", Collections.emptyList());
        Violation shouldViolation = new Violation(dummyRule, "", "", ViolationType.SHOULD, "", Collections.emptyList());

        ApiReview apiReview = new ApiReview(new ApiDefinitionRequest(), "", asList(mustViolation1, mustViolation2, shouldViolation));

        assertThat(apiReview.getMustViolations()).isEqualTo(2);
        assertThat(apiReview.getShouldViolations()).isEqualTo(1);
        assertThat(apiReview.getMayViolations()).isEqualTo(0);
        assertThat(apiReview.getHintViolations()).isEqualTo(0);
    }

    @Test
    public void shouldCalculateNumberOfEndpoints() throws IOException {
        Violation violation1 = new Violation(dummyRule, "", "", ViolationType.MUST, "", asList("1", "2"));
        Violation violation2 = new Violation(dummyRule, "", "", ViolationType.MUST, "", asList("3"));

        String apiDefinition = ResourceUtil.resourceToString("fixtures/limitNumberOfResourcesValid.json");

        ApiReview apiReview = new ApiReview(new ApiDefinitionRequest(), apiDefinition, asList(violation1, violation2));

        assertThat(apiReview.getNumberOfEndpoints()).isEqualTo(2);
    }

    @Test
    public void shouldParseApiNameFromApiDefinition() throws IOException {
        String apiDefinition = ResourceUtil.resourceToString("fixtures/limitNumberOfResourcesValid.json");
        ApiReview apiReview = new ApiReview(new ApiDefinitionRequest(), apiDefinition, Collections.emptyList());
        assertThat(apiReview.getName()).isEqualTo("Test Service");
    }
}

package de.zalando.zally.apireview;

import de.zalando.zally.dto.ApiDefinitionResponse;
import de.zalando.zally.dto.ViolationDTO;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static de.zalando.zally.util.ResourceUtil.readApiDefinition;
import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = "zally.ignoreRules=H999")
public class RestApiIgnoreRulesTest extends RestApiBaseTest {

    @Test
    public void shouldIgnoreSpecifiedRules() throws Exception {
        ApiDefinitionResponse response = sendApiDefinition(readApiDefinition("fixtures/api_spp.json"));

        List<ViolationDTO> violations = response.getViolations();
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getTitle()).isEqualTo("dummy1");

        Map<String, Integer> count = response.getViolationsCount();
        assertThat(count.get("must")).isEqualTo(1);
        assertThat(count.get("should")).isEqualTo(0);
        assertThat(count.get("may")).isEqualTo(0);
        assertThat(count.get("hint")).isEqualTo(0);
    }
}

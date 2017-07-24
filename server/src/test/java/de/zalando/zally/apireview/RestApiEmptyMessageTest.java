package de.zalando.zally.apireview;

import de.zalando.zally.dto.ApiDefinitionResponse;
import de.zalando.zally.dto.ViolationDTO;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

import static de.zalando.zally.util.ResourceUtil.readApiDefinition;
import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = "zally.message=")
public class RestApiEmptyMessageTest extends RestApiBaseTest {

    @Test
    public void shouldNotContainMessageFieldWhenMessageIsEmpty() throws IOException {
        ApiDefinitionResponse response = sendApiDefinition(readApiDefinition("fixtures/api_spp.json"));

        List<ViolationDTO> violations = response.getViolations();
        assertThat(violations).isNotEmpty();
        assertThat(response.getMessage()).isEmpty();
    }
}

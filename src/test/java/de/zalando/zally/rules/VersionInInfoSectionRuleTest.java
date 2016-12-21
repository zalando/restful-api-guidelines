package de.zalando.zally.rules;

import io.swagger.models.Info;
import io.swagger.models.Swagger;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VersionInInfoSectionRuleTest {

    private Rule rule = new VersionInInfoSectionRule();
    private Swagger testSwagger = new Swagger();
    private Info testInfo = new Info();

    @Test
    public void forEmptySwagger() {
        assertThat(rule.validate(testSwagger)).isEmpty();
    }

    @Test
    public void forInfoSectionWithCorrectVersion() {
        testInfo.setVersion("1.2.3");
        testSwagger.info(testInfo);
        assertThat(rule.validate(testSwagger)).isEmpty();
    }

    @Test
    public void forMissingVersionInInfoSection() {
        testSwagger.info(testInfo);
        String expectedDescription =  "Only the documentation, not the API itself, needs version information. It should be in the format MAYOR.MINOR.DRAFT\nVersion is missing.";
        assertThat(rule.validate(testSwagger).get(0).getDescription()).isEqualTo(expectedDescription);
    }

    @Test
    public void forWrongVersionInInfoSection() {
        testInfo.setVersion("1.2.3-a");
        testSwagger.info(testInfo);
        String expectedDescription =  "Only the documentation, not the API itself, needs version information. It should be in the format MAYOR.MINOR.DRAFT\nVersion is not in the correct format.";
        assertThat(rule.validate(testSwagger).get(0).getDescription()).isEqualTo(expectedDescription);
    }
}

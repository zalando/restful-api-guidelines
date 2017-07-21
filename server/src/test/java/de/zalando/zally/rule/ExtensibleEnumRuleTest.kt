package de.zalando.zally.rule

import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import de.zalando.zally.getFixture
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExtensibleEnumRuleTest {

    val rule = ExtensibleEnumRule()

    @Test
    fun returnsNoViolationIfEmptySwagger() {
        assertThat(rule.validate(Swagger())).isNull()
    }

    @Test
    fun returnsViolationIfAnEnumInModelProperty() {
        val swagger = getFixture("enum_in_model_property.yaml")
        val expectedViolation = Violation(rule=rule,
            title=rule.title,
            violationType = ViolationType.SHOULD,
            ruleLink = rule.url,
            description = "Properties/Parameters [status] are not extensible enums",
            paths = listOf("#/definitions/CrawledAPIDefinition/properties/status"))

        val violation = rule.validate(swagger)

        assertThat(violation).isNotNull()
        assertThat(violation).isEqualTo(expectedViolation)
    }

    @Test
    fun returnsViolationIfAnEnumInRequestParameter() {
        val swagger = getFixture("enum_in_request_parameter.yaml")
        val expectedViolation = Violation(rule = rule,
            title = rule.title,
            violationType = ViolationType.SHOULD,
            ruleLink = rule.url,
            description = "Properties/Parameters [lifecycle_state, environment] are not extensible enums",
            paths = listOf("#/paths/apis/{api_id}/versions/GET/parameters/lifecycle_state",
                "#/paths/apis/GET/parameters/environment"))

        val violation = rule.validate(swagger)

        assertThat(violation).isNotNull()
        assertThat(violation).isEqualTo(expectedViolation)
    }

    @Test
    fun returnsNoViolationIfNoEnums() {
        val swagger = getFixture("no_must_violations.yaml")

        assertThat(rule.validate(swagger)).isNull()
    }

}

package de.zalando.zally.rule

import de.zalando.zally.getFixture
import de.zalando.zally.testConfig
import de.zalando.zally.violation.Violation
import de.zalando.zally.violation.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.properties.StringProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.format.annotation.DateTimeFormat

class ExtensibleEnumRuleTest {

    val rule = ExtensibleEnumRule()

    @Test
    fun returnsNoViolationIfEmptySwagger() {
        assertThat(rule.validate(Swagger())).isNull()
    }

    @Test
    fun returnsViolationIfAnEnumInModelProperty() {
        val swagger = getFixture("enum_in_model_property.yaml")
        val expectedViolation = Violation(rule, rule.title, "Properties/Fields [status] are not extensible enums",
            ViolationType.SHOULD, rule.url, listOf("/api-definitions"))

        val violation = rule.validate(swagger)

        assertThat(violation).isNotNull()
        assertThat(violation).isEqualTo(expectedViolation)
    }

    @Test
    fun returnsViolationIfAnEnumInRequestParameter() {
        val swagger = getFixture("enum_in_request_parameter.yaml")

        val expectedViolation = Violation(rule, rule.title,
            "Properties/Fields [lifecycle_state] are not extensible enums", ViolationType.SHOULD, rule.url,
            listOf("/apis", "/apis/{api_id}/versions"))

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

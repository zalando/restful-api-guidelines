package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.pp
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RulesValidatorTest {
    val DUMMY_VIOLATION_1 = Violation("dummy1", "dummy", ViolationType.MUST, "dummy", listOf("a"))
    val DUMMY_VIOLATION_2 = Violation("dummy2", "dummy", ViolationType.MUST, "dummy", listOf("x", "y", "z"))

    val swaggerContent = javaClass.classLoader.getResource("fixtures/api_spp.json").readText(Charsets.UTF_8).pp

    class TestRule(val result: Violation?) : Rule {
        override fun validate(swagger: Swagger): Violation? = result
    }

    @Test
    fun shouldReturnEmptyViolationsListWithoutRules() {
        val validator = RulesValidator(emptyList())
        assertThat(validator.validate(swaggerContent)).isEmpty()
    }

    @Test
    fun shouldReturnOneViolation() {
        val violations = listOf(DUMMY_VIOLATION_1)
        val validator = RulesValidator(violations.map { TestRule(it) })
        assertThat(validator.validate(swaggerContent)).hasSameElementsAs(violations)
    }

    @Test
    fun shouldCollectViolationsOfAllRules() {
        val violations = listOf(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2)
        val validator = RulesValidator(violations.map { TestRule(it) })
        assertThat(validator.validate(swaggerContent)).hasSameElementsAs(violations)
    }
}

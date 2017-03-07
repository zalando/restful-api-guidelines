package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RulesValidatorTest {

    val DUMMY_VIOLATION_1 = Violation(TestRule(null), "dummy1", "dummy", ViolationType.SHOULD, "dummy", listOf("x", "y", "z"))
    val DUMMY_VIOLATION_2 = Violation(TestRule(null), "dummy2", "dummy", ViolationType.COULD, "dummy", listOf())
    val DUMMY_VIOLATION_3 = Violation(TestRule(null), "dummy3", "dummy", ViolationType.MUST, "dummy", listOf("a"))

    val swaggerContent = javaClass.classLoader.getResource("fixtures/api_spp.json").readText(Charsets.UTF_8)

    class TestRule(val result: Violation?) : AbstractRule() {
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

    @Test
    fun shouldSortViolationsByViolationType() {
        val violations = listOf(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2, DUMMY_VIOLATION_3)
        val validator = RulesValidator(violations.map { TestRule(it) })
        assertThat(validator.validate(swaggerContent))
                .containsExactly(DUMMY_VIOLATION_3, DUMMY_VIOLATION_1, DUMMY_VIOLATION_2)
    }
}

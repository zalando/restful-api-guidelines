package de.zalando.zally.rule

import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RulesValidatorTest {

    val DUMMY_VIOLATION_1 = Violation(FirstRule(null), "dummy1", "dummy", ViolationType.SHOULD, "dummy", listOf("x", "y", "z"))
    val DUMMY_VIOLATION_2 = Violation(FirstRule(null), "dummy2", "dummy", ViolationType.MAY, "dummy", listOf())
    val DUMMY_VIOLATION_3 = Violation(SecondRule(null), "dummy3", "dummy", ViolationType.MUST, "dummy", listOf("a"))

    val swaggerContent = javaClass.classLoader.getResource("fixtures/api_spp.json").readText(Charsets.UTF_8)

    class FirstRule(val result: Violation?) : AbstractRule() {
        override val title = "First Rule"
        override val url = null
        override val violationType = ViolationType.SHOULD
        override val code = "S999"
        override fun validate(swagger: Swagger): Violation? = result
    }

    class SecondRule(val result: Violation?) : AbstractRule() {
        override val title = "Second Rule"
        override val url = null
        override val violationType = ViolationType.MUST
        override val code = "M999"
        override fun validate(swagger: Swagger): Violation? = result
    }

    @Test
    fun shouldReturnEmptyViolationsListWithoutRules() {
        val validator = RulesValidator(emptyList(), RulesPolicy(emptyArray()))
        assertThat(validator.validate(swaggerContent)).isEmpty()
    }

    @Test
    fun shouldReturnOneViolation() {
        val violations = listOf(DUMMY_VIOLATION_1)
        val validator = RulesValidator(getRules(violations), RulesPolicy(emptyArray()))
        assertThat(validator.validate(swaggerContent)).hasSameElementsAs(violations)
    }

    @Test
    fun shouldCollectViolationsOfAllRules() {
        val violations = listOf(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2)
        val validator = RulesValidator(getRules(violations), RulesPolicy(emptyArray()))
        assertThat(validator.validate(swaggerContent)).hasSameElementsAs(violations)
    }

    @Test
    fun shouldSortViolationsByViolationType() {
        val violations = listOf(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2, DUMMY_VIOLATION_3)
        val validator = RulesValidator(getRules(violations), RulesPolicy(emptyArray()))
        assertThat(validator.validate(swaggerContent))
            .containsExactly(DUMMY_VIOLATION_3, DUMMY_VIOLATION_1, DUMMY_VIOLATION_2)
    }

    @Test
    fun shouldIgnoreSpecifiedRules() {
        val violations = listOf(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2, DUMMY_VIOLATION_3)
        val validator = RulesValidator(getRules(violations), RulesPolicy(arrayOf("M999")))
        assertThat(validator.validate(swaggerContent)).containsExactly(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2)
    }

    fun getRules(violations: List<Violation>): List<Rule> {
        return violations.map {
            if (it.rule is FirstRule) {
                FirstRule(it)
            } else {
                SecondRule(it)
            }
        }
    }
}

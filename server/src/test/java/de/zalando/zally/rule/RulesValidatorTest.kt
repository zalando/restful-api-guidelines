package de.zalando.zally.rule

import de.zalando.zally.dto.ViolationType
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class RulesValidatorTest {

    val DUMMY_VIOLATION_1 = Violation(FirstRule(null), "dummy1", "dummy", ViolationType.SHOULD, "dummy", listOf("x", "y", "z"))
    val DUMMY_VIOLATION_2 = Violation(FirstRule(null), "dummy2", "dummy", ViolationType.MAY, "dummy", listOf())
    val DUMMY_VIOLATION_3 = Violation(SecondRule(null), "dummy3", "dummy", ViolationType.MUST, "dummy", listOf("a"))

    val swaggerContent = javaClass.classLoader.getResource("fixtures/api_spp.json").readText(Charsets.UTF_8)

    class FirstRule(val result: Violation?) : SwaggerRule() {
        override val title = "First Rule"
        override val url = null
        override val violationType = ViolationType.SHOULD
        override val code = "S999"
        override fun validate(swagger: Swagger): Violation? = result
    }

    class SecondRule(val result: Violation?) : SwaggerRule() {
        override val title = "Second Rule"
        override val url = null
        override val violationType = ViolationType.MUST
        override val code = "M999"
        override fun validate(swagger: Swagger): Violation? = result
    }

    val invalidApiSchemaRule: InvalidApiSchemaRule = mock(InvalidApiSchemaRule::class.java)

    @Test
    fun shouldReturnEmptyViolationsListWithoutRules() {
        val validator = SwaggerRulesValidator(emptyList(), RulesPolicy(emptyArray()), invalidApiSchemaRule)
        assertThat(validator.validate(swaggerContent)).isEmpty()
    }

    @Test
    fun shouldReturnOneViolation() {
        val violations = listOf(DUMMY_VIOLATION_1)
        val validator = SwaggerRulesValidator(getRules(violations), RulesPolicy(emptyArray()), invalidApiSchemaRule)
        assertThat(validator.validate(swaggerContent)).hasSameElementsAs(violations)
    }

    @Test
    fun shouldCollectViolationsOfAllRules() {
        val violations = listOf(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2)
        val validator = SwaggerRulesValidator(getRules(violations), RulesPolicy(emptyArray()), invalidApiSchemaRule)
        assertThat(validator.validate(swaggerContent)).hasSameElementsAs(violations)
    }

    @Test
    fun shouldSortViolationsByViolationType() {
        val violations = listOf(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2, DUMMY_VIOLATION_3)
        val validator = SwaggerRulesValidator(getRules(violations), RulesPolicy(emptyArray()), invalidApiSchemaRule)
        assertThat(validator.validate(swaggerContent))
                .containsExactly(DUMMY_VIOLATION_3, DUMMY_VIOLATION_1, DUMMY_VIOLATION_2)
    }

    @Test
    fun shouldIgnoreSpecifiedRules() {
        val violations = listOf(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2, DUMMY_VIOLATION_3)
        val validator = SwaggerRulesValidator(getRules(violations), RulesPolicy(arrayOf("M999")), invalidApiSchemaRule)
        assertThat(validator.validate(swaggerContent)).containsExactly(DUMMY_VIOLATION_1, DUMMY_VIOLATION_2)
    }

    @Test
    fun shouldReturnInvalidApiSchemaRuleForBadSwagger() {
        val resultRule = mock(InvalidApiSchemaRule::class.java)
        Mockito.`when`(resultRule.title).thenReturn("InvalidApiSchemaRule Title")
        Mockito.`when`(resultRule.description).thenReturn("desc")
        Mockito.`when`(resultRule.violationType).thenReturn(ViolationType.MUST)
        Mockito.`when`(resultRule.url).thenReturn("url")

        val validator = SwaggerRulesValidator(emptyList(), RulesPolicy(emptyArray()), resultRule)
        val valResult = validator.validate("Invalid swagger content !@##")
        assertThat(valResult).hasSize(1)
        assertThat(valResult[0].title).isEqualTo(resultRule.title)
    }

    fun getRules(violations: List<Violation>): List<SwaggerRule> {
        return violations.map {
            if (it.rule is FirstRule) {
                FirstRule(it)
            } else {
                SecondRule(it)
            }
        }
    }
}

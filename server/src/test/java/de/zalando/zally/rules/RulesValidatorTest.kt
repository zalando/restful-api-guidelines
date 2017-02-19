package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class RulesValidatorTest {
    val DUMMY_VIOLATION_1 = Violation("dummy1", "dummy", ViolationType.MUST, "dummy", emptyList())
    val DUMMY_VIOLATION_2 = Violation("dummy2", "dummy", ViolationType.MUST, "dummy", emptyList())
    val DUMMY_VIOLATION_PATH_A = Violation("dummy3", "dummy", ViolationType.MUST, "dummy", listOf("a"))
    val DUMMY_VIOLATION_PATH_Z = Violation("dummy3", "dummy", ViolationType.MUST, "dummy", listOf("z"))

    val swaggerContent = Files.readAllBytes(Paths.get(
            ClassLoader.getSystemResource("resources/api_petstore.json").toURI())).toString(Charsets.UTF_8)

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

    @Test
    fun shouldSortViolationsAlphabeticallyAndWithoutPathOnTop() {
        val violations = listOf(DUMMY_VIOLATION_PATH_A, DUMMY_VIOLATION_1, DUMMY_VIOLATION_PATH_Z)
        val validator = RulesValidator(violations.map { TestRule(it) })
        assertThat(validator.validate(swaggerContent)).containsExactly(DUMMY_VIOLATION_1, DUMMY_VIOLATION_PATH_A,
                DUMMY_VIOLATION_PATH_Z)
    }
}

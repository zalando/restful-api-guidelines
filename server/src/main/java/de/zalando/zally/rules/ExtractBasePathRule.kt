package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class ExtractBasePathRule : Rule {
    val TITLE = "Base path can be extracted"
    val DESC_PATTERN = "All paths start with prefix '%s'. This prefix could be part of base path."
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html"

    override fun validate(swagger: Swagger): List<Violation> {
        val paths = swagger.paths?.keys
        if (paths == null || paths.isEmpty()) {
            return emptyList()
        }
        val commonPrefix: String = paths.reduce { s1, s2 -> findCommonPrefix(s1, s2) }
        return if (commonPrefix.isNotEmpty() && commonPrefix != "/") {
            listOf(createViolation(commonPrefix.dropTrailingSlash()))
        } else emptyList()
    }

    private fun String.dropTrailingSlash() = if (last() == '/') dropLast(1) else this

    private fun createViolation(commonPrefix: String) =
            Violation(TITLE, DESC_PATTERN.format(commonPrefix), ViolationType.HINT, RULE_URL)

    private fun findCommonPrefix(s1: String, s2: String): String {
        val endIndex = Math.min(s1.length, s2.length) - 1
        val prefixLength = (0..endIndex).takeWhile { i -> s1[i] == s2[i] && s1[i] != '{' }.last()
        return s1.take(prefixLength + 1)
    }
}

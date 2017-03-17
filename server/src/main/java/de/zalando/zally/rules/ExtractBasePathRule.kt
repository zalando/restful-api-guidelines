package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class ExtractBasePathRule : AbstractRule() {

    override val title = "Base path can be extracted"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html"
    override val violationType = ViolationType.HINT
    val DESC_PATTERN = "All paths start with prefix '%s'. This prefix could be part of base path."

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys
        if (paths.isEmpty()) {
            return null
        }
        val commonPrefix = paths.reduce { s1, s2 -> findCommonPrefix(s1, s2) }.dropTrailingSlash()
        return if (commonPrefix.isNotEmpty() && hasSubResources(swagger, commonPrefix))
            Violation(this, title, DESC_PATTERN.format(commonPrefix), violationType, url, emptyList())
        else null
    }

    private fun hasSubResources(swagger: Swagger, commonPrefix: String): Boolean {
        return swagger.paths.filterKeys {
            it.startsWith(commonPrefix) && it.indexOf("/", commonPrefix.length + 1) != -1
        }.isNotEmpty()
    }

    private fun String.dropTrailingSlash() = if (last() == '/') dropLast(1) else this

    private fun findCommonPrefix(s1: String, s2: String): String {
        val endIndex = Math.min(s1.length, s2.length) - 1
        val prefixLength = (0..endIndex).takeWhile { i -> s1[i] == s2[i] && s1[i] != '{' }.last()
        return s1.take(prefixLength + 1)
    }
}

package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger

/**
 * TODO:
 *      - this rule is currently disabled because Zalando's API guidelines changed
 *      - will be re-enabled if we can configure ruleset
 */
class AvoidJavascriptKeywordsRule : AbstractRule() {

    override val title = "Avoid reserved Javascript keywords"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#should-reserved-javascript-keywords-should-be-avoided"
    override val violationType = ViolationType.SHOULD
    override val code = "S001"
    private val DESC_PATTERN = "Property names should not coinside with reserved javascript keywords"

    private val RESERVED_KEYWORDS = setOf(
            "break", "do", "in", "typeof", "case", "else", "instanceof", "var", "catch", "export", "new", "void",
            "class", "extends", "return", "while", "const", "finally", "super", "with", "continue", "for", "switch",
            "yield", "debugger", "function", "this", "default", "if", "throw", "delete", "import", "try"
    )

    override fun validate(swagger: Swagger): Violation? {
        val definitions = swagger.definitions ?: emptyMap()
        val paths = definitions.flatMap { entry ->
            val props = entry.value.properties ?: emptyMap()
            props.keys.filter { it in RESERVED_KEYWORDS }.map { entry.key + "." + it }
        }
        return if (paths.isNotEmpty()) Violation(this, title, DESC_PATTERN, violationType, url, paths) else null
    }
}
package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger

/**
 * TODO:
 *      - this rule is currently disabled because Zalando's API guidelines changed
 *      - will be re-enabled if we can configure ruleset
 */
open class AvoidJavascriptKeywordsRule : AbstractRule() {

    val TITLE = "Avoid reserved Javascript keywords"
    val DESC_PATTERN = "Property names should not coinside with reserved javascript keywords"
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#should-reserved-javascript-keywords-should-be-avoided"

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
        return if (paths.isNotEmpty()) Violation(this, TITLE, DESC_PATTERN, ViolationType.SHOULD, RULE_URL, paths) else null
    }
}
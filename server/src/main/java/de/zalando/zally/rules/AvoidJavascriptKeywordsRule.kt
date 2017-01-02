package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Model
import io.swagger.models.Swagger

import java.util.*

class AvoidJavascriptKeywordsRule : Rule {
    val TITLE = "Avoid reserved Javascript keywords"
    val DESC_PATTERN = "Property name '%s' is reserved javascript keyword"
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#should-reserved-javascript-keywords-should-be-avoided"
    private val RESERVED_KEYWORDS = setOf(
            "break", "do", "in", "typeof", "case", "else", "instanceof", "var", "catch", "export", "new", "void",
            "class", "extends", "return", "while", "const", "finally", "super", "with", "continue", "for", "switch",
            "yield", "debugger", "function", "this", "default", "if", "throw", "delete", "import", "try"
    )

    override fun validate(swagger: Swagger): List<Violation> {
        val definitions = swagger.definitions ?: emptyMap()
        val res = definitions.flatMap { entry ->
            val props = entry.value.properties ?: emptyMap()
            props.keys.filter { RESERVED_KEYWORDS.contains(it) }.map { createViolation(entry.key, it) }
        }
        return res
    }

    private fun createViolation(path: String, fieldName: String) =
            Violation(TITLE, DESC_PATTERN.format(fieldName), ViolationType.SHOULD, RULE_URL, path + "." + fieldName)
}

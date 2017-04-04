package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.getAllJsonObjects
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class AvoidJavascriptKeywordsRule : AbstractRule() {

    override val title = "Avoid reserved Javascript keywords"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#should-reserved-javascript-keywords-should-be-avoided"
    override val violationType = ViolationType.SHOULD
    override val code = "S001"
    private val DESC_PATTERN = "Property names should not coincide with reserved javascript keywords"

    private val RESERVED_KEYWORDS = setOf(
            "break", "do", "in", "typeof", "case", "else", "instanceof", "var", "catch", "export", "new", "void",
            "class", "extends", "return", "while", "const", "finally", "super", "with", "continue", "for", "switch",
            "yield", "debugger", "function", "this", "default", "if", "throw", "delete", "import", "try"
    )

    override fun validate(swagger: Swagger): Violation? {
        val result = swagger.getAllJsonObjects().flatMap { (def, path) ->
            val badProps = def.keys.filter { it in RESERVED_KEYWORDS }
            if (badProps.isNotEmpty()) listOf(path + ": " + badProps.joinToString(", ") to path) else emptyList()
        }
        return if (result.isNotEmpty()) {
            val (props, paths) = result.unzip()
            Violation(this, title, DESC_PATTERN + "\n" + props.joinToString("\n"), violationType, url, paths)
        } else null
    }
}
package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.WordUtil.isPlural
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class PluralizeNamesForArraysRule : Rule {
    val TITLE = "Array names should be pluralized"
    val DESC_PATTERN = "Array property name '%s' is not pluralized (but we are not sure)"
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#should-array-names-should-be-pluralized"

    override fun validate(swagger: Swagger): List<Violation> {
        val definitions = swagger.definitions ?: emptyMap()
        return definitions.flatMap { entry ->
            val props = entry.value?.properties ?: emptyMap()
            props.filter {"array" == it.value.type && !isPlural(it.key)}.map { createViolation(entry.key, it.key) }
        }
    }

    private fun createViolation(path: String, fieldName: String) =
            Violation(TITLE, DESC_PATTERN.format(fieldName), ViolationType.SHOULD, RULE_URL, path + "." + fieldName)
}

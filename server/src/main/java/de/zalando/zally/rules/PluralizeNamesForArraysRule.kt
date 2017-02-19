package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.WordUtil.isPlural
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class PluralizeNamesForArraysRule : Rule {
    val TITLE = "Array names should be pluralized"
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#should-array-names-should-be-pluralized"

    override fun validate(swagger: Swagger): Violation? {
        val definitions = swagger.definitions.orEmpty()
        val res = definitions.entries.map { def ->
            val badProps = def.value.properties.entries.filter { "array" == it.value.type && !isPlural(it.key) }
            if (badProps.isNotEmpty())
                Pair("Definition ${def.key}: " + badProps.joinToString("\n"), "#/definitions/${def.key}")
            else null
        }.filterNotNull()

        return if (res.isNotEmpty()) {
            val (desc, paths) = res.unzip()
            Violation(TITLE, desc.joinToString("\n"), ViolationType.SHOULD, RULE_URL, paths)
        } else null
    }
}

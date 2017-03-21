package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.WordUtil.isPlural
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class PluralizeNamesForArraysRule : AbstractRule() {
    override val title = "Array names should be pluralized"
    override val url = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#should-array-names-should-be-pluralized"
    override val violationType = ViolationType.SHOULD
    override val code = "S007"

    override fun validate(swagger: Swagger): Violation? {
        val res = swagger.definitions.orEmpty().entries.map { def ->
            val badProps = def.value?.properties.orEmpty().entries.filter { "array" == it.value?.type && !isPlural(it.key) }
            if (badProps.isNotEmpty()) {
                val propsDesc = badProps.map { "'${it.key}'" }.joinToString(",")
                "Definition ${def.key}: $propsDesc" to "#/definitions/${def.key}"
            } else null
        }.filterNotNull()

        return if (res.isNotEmpty()) {
            val (desc, paths) = res.unzip()
            Violation(this, title, desc.joinToString("\n"), violationType, url, paths)
        } else null
    }
}

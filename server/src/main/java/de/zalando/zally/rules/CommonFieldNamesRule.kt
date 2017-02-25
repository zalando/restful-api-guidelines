package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.properties.Property
import io.swagger.models.properties.StringProperty
import org.springframework.stereotype.Component

@Component
open class CommonFieldNamesRule : Rule {
    private val title = "Use common field names"
    private val link = "http://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html" +
            "#must-use-common-field-names"

    override fun validate(swagger: Swagger): Violation? {
        val definitions = swagger.definitions.orEmpty()
        val res = definitions.entries.map { def ->
            val badProps = def.value.properties.entries.map { checkField(it.key, it.value) }.filterNotNull()
            if (badProps.isNotEmpty()) {
                val propsDesc = badProps.joinToString("\n")
                "Definition ${def.key}: $propsDesc" to "#/definitions/${def.key}"
            } else null
        }.filterNotNull()

        return if (res.isNotEmpty()) {
            val (desc, paths) = res.unzip()
            Violation(title, desc.joinToString("\n"), ViolationType.MUST, link, paths)
        } else null
    }

    companion object {
        private val commonFields = mapOf<String, Property>(
                "id" to StringProperty(),
                "created" to StringProperty("date-time"),
                "modified" to StringProperty("date-time"),
                "type" to StringProperty()
        )

        fun checkField(name: String, property: Property): String? =
                commonFields[name.toLowerCase()]?.let { common ->
                    if (property.type != common.type)
                        "field '$name' has type '${property.type}' (expected type '${common.type}')"
                    else if (property.format != common.format && common.format != null)
                        "field '$name' has type '${property.type}' with format '${property.format}' " +
                                "(expected format '${common.format}')"
                    else null
                }
    }
}

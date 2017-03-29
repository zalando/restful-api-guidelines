package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.getAllJsonObjects
import io.swagger.models.Swagger
import io.swagger.models.properties.Property
import io.swagger.models.properties.StringProperty
import org.springframework.stereotype.Component

@Component
class CommonFieldNamesRule : AbstractRule() {
    override val title = "Use common field names"
    override val url = "http://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html" +
            "#must-use-common-field-names"
    override val violationType = ViolationType.MUST
    override val code = "M003"

    override fun validate(swagger: Swagger): Violation? {
        val res = swagger.getAllJsonObjects().map { (def, path) ->
            val badProps = def.entries.map { checkField(it.key, it.value) }.filterNotNull()
            if (badProps.isNotEmpty())
                (path + ": " + badProps.joinToString(", ")) to path
            else null
        }.filterNotNull()

        return if (res.isNotEmpty()) {
            val (desc, paths) = res.unzip()
            Violation(this, title, desc.joinToString(", "), violationType, url, paths)
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

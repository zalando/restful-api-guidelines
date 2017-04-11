package de.zalando.zally.rules

import com.typesafe.config.Config
import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.getAllJsonObjects
import io.swagger.models.Swagger
import io.swagger.models.properties.Property
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CommonFieldTypesRule(@Autowired rulesConfig: Config) : AbstractRule() {
    override val title = "Use common field names"
    override val url = "http://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html" +
            "#must-use-common-field-names"
    override val violationType = ViolationType.MUST
    override val code = "M003"

    @Suppress("UNCHECKED_CAST")
    private val commonFields = rulesConfig.getConfig("$name.common_types").entrySet()
            .map { (key, config) -> key to config.unwrapped() as List<String?> }.toMap()

    fun checkField(name: String, property: Property): String? =
            commonFields[name.toLowerCase()]?.let { (type, format) ->
                if (property.type != type)
                    "field '$name' has type '${property.type}' (expected type '$type')"
                else if (property.format != format && format != null)
                    "field '$name' has type '${property.type}' with format '${property.format}' (expected format '$format')"
                else null
            }

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

}

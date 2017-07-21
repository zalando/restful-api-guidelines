package de.zalando.zally.rule

import de.zalando.zally.dto.ViolationType
import io.swagger.models.ComposedModel
import io.swagger.models.ModelImpl
import io.swagger.models.Swagger
import io.swagger.models.properties.Property
import io.swagger.models.properties.RefProperty
import org.springframework.stereotype.Component

@Component
class SuccessResponseAsJsonObjectRule : SwaggerRule() {

    override val title = "Response As JSON Object"
    override val violationType = ViolationType.MUST
    override val url = "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
        "#must-always-return-json-objects-as-toplevel-data-structures-to-support-extensibility"
    override val code = "M013"
    private val DESCRIPTION = "Always Return JSON Objects As Top-Level Data Structures To Support Extensibility"

    override fun validate(swagger: Swagger): Violation? {
        val paths = ArrayList<String>()
        for ((key, value) in swagger.paths.orEmpty()) {
            for ((method, operation) in value.operationMap) {
                for ((code, response) in operation.responses) {
                    val httpCodeInt = code.toIntOrZero()
                    if (httpCodeInt in 200..299) {
                        val schema = response.schema
                        if (schema != null && "object" != schema.type && !schema.isRefToObject(swagger)) {
                            paths.add("$key $method $code")
                        }
                    }
                }
            }
        }

        return if (paths.isNotEmpty()) Violation(this, title, DESCRIPTION, violationType, url, paths) else null
    }

    private fun Property?.isRefToObject(swagger: Swagger) =
        if (this is RefProperty && swagger.definitions != null) {
            val model = swagger.definitions[simpleRef]
            (model is ModelImpl && model.type == "object") || model is ComposedModel
        } else false

    private fun String.toIntOrZero() =
        try {
            this.toInt()
        } catch (e: NumberFormatException) {
            0
        }
}

package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.ModelImpl
import io.swagger.models.Swagger
import io.swagger.models.properties.Property
import io.swagger.models.properties.RefProperty
import org.springframework.stereotype.Component
import java.util.*


@Component
open class SuccessResponseAsJsonObjectRule : Rule {
    private val TITLE = "Response As JSON Object"
    private val DESCRIPTION = "Always Return JSON Objects As Top-Level Data Structures To Support Extensibility"
    private val VIOLATION_TYPE = ViolationType.MUST
    private val RULE_LINK = "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
            "#must-always-return-json-objects-as-toplevel-data-structures-to-support-extensibility"

    override fun validate(swagger: Swagger): List<Violation> {
        val violations = ArrayList<Violation>()
        val paths = swagger.paths ?: emptyMap()
        for ((key, value) in paths) {
            for ((method, operation) in value.operationMap) {
                for ((code, response) in operation.responses) {
                    val httpCodeInt = code.toIntOrZero()
                    if (httpCodeInt in 200..299) {
                        val schema = response.schema
                        if ("object" != schema?.type && !schema.isRefToObject(swagger)) {
                            violations.add(Violation(TITLE, DESCRIPTION, VIOLATION_TYPE, RULE_LINK, "$key/$method/$code"))
                        }
                    }
                }
            }
        }

        return violations
    }

    private fun Property?.isRefToObject(swagger: Swagger) =
        if (this is RefProperty) {
            val model = swagger.definitions[simpleRef]
            model is ModelImpl && model.type == "object"
        } else false

    private fun String.toIntOrZero() =
        try {
            this.toInt()
        } catch(e: NumberFormatException) {
            0
        }
}

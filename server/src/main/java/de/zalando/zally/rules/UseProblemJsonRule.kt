package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Model
import io.swagger.models.Response
import io.swagger.models.Swagger
import io.swagger.models.properties.RefProperty
import org.springframework.stereotype.Component

@Component
class UseProblemJsonRule : AbstractRule() {
    override val title = "Use Problem JSON"
    override val url = "https://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html" +
            "#must-use-problem-json"
    override val violationType = ViolationType.MUST
    override val code = "M015"
    private val DESCRIPTION = "Operations Should Return Problem JSON When Any Problem Occurs During Processing Whether Caused " +
            "by Client Or Server"

    override fun validate(swagger: Swagger): Violation? {
        val isProblemDefined = isProblemDefinitionCorrect(swagger.definitions.get("Problem"))
        val paths = swagger.paths.orEmpty().flatMap { pathEntry ->
            pathEntry.value.operationMap.orEmpty().flatMap { opEntry ->
                opEntry.value.responses.orEmpty().flatMap { responseEntry ->
                    val httpCode = responseEntry.key.parseInt()
                    if (httpCode in 400..599 && (!isProperRef(responseEntry.value) || !isProblemDefined)) {
                        listOf("${pathEntry.key} ${opEntry.key} ${responseEntry.key}")
                    } else emptyList()
                }
            }
        }

        return if (paths.isNotEmpty()) Violation(this, title, DESCRIPTION, violationType, url, paths) else null
    }

    private fun String.parseInt(): Int? =
            try {
                toInt()
            } catch (nfe: NumberFormatException) {
                null
            }

    private fun isProperRef(response: Response) : Boolean {
        return if (response.schema?.type == "ref") {
            val schema : RefProperty = response.schema as RefProperty
            (schema.`$ref` == "#/definitions/Problem")
        } else {
            false
        }
    }

    private fun isProblemDefinitionCorrect(definition: Model?) : Boolean {
        return (definition?.properties?.keys == setOf("type", "title", "status", "detail", "instance"))
    }
}

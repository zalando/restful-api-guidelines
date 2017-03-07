package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class UseProblemJsonRule : AbstractRule() {
    val TITLE = "Use Problem JSON"
    val DESCRIPTION = "Operations Should Return Problem JSON When Any Problem Occurs During Processing Whether Caused " +
            "by Client Or Server"
    val RULE_LINK = "https://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html" +
            "#must-use-problem-json"

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().flatMap { pathEntry ->
            pathEntry.value.operationMap.orEmpty().flatMap { opEntry ->
                opEntry.value.responses.orEmpty().flatMap { responseEntry ->
                    val httpCode = responseEntry.key.parseInt()
                    if (httpCode in 400..599 && responseEntry.value?.schema?.type != "ref") { //TODO MK: implement real check for Problem usage
                        listOf("${pathEntry.key} ${opEntry.key} ${responseEntry.key}")
                    } else emptyList()
                }
            }
        }
        return if (paths.isNotEmpty()) Violation(this, TITLE, DESCRIPTION, ViolationType.MUST, RULE_LINK, paths) else null
    }

    private fun String.parseInt(): Int? =
            try {
                toInt()
            } catch (nfe: NumberFormatException) {
                null
            }
}

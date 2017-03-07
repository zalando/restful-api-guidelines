package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.parser.SwaggerParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * This validator validates a given Swagger definition based
 * on set of rules. It will sort the output by path.
 */
@Component
open class RulesValidator(@Autowired val rules: List<Rule>) {

    fun validate(swaggerContent: String): List<Violation> {
        val swagger = try {
            SwaggerParser().parse(swaggerContent)!!
        } catch (e: Exception) {
            return listOf(Violation(InvalidSwaggerFileDummyRule(), "Can't parse swagger file",
                    e.toString(), ViolationType.MUST, "", emptyList()))
        }
        return rules.map { it.validate(swagger) }.filterNotNull().sortedBy { it.violationType }
    }
}

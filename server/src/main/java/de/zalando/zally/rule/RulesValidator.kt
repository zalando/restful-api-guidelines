package de.zalando.zally.rule

import io.swagger.parser.SwaggerParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * This validator validates a given Swagger definition based
 * on set of rules. It will sort the output by path.
 */
@Component
class RulesValidator(@Autowired val rules: List<Rule>, @Autowired val rulesPolicy: RulesPolicy) {

    fun validate(swaggerContent: String): List<Violation> {
        val swagger = try {
            SwaggerParser().parse(swaggerContent)!!
        } catch (e: Exception) {
            return listOf(InvalidApiSchemaRule().validate())
        }
        return rules
            .filter { rule -> rulesPolicy.accepts(rule) }
            .map { it.validate(swagger) }
            .filterNotNull()
            .sortedBy { it.violationType }
    }
}

package de.zalando.zally.rule

import io.swagger.parser.SwaggerParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * This validator validates a given Swagger definition based
 * on set of rules. It will sort the output by path.
 */
@Component
class SwaggerRulesValidator(
        @Autowired val rules: List<SwaggerRule>,
        @Autowired val rulesPolicy: RulesPolicy) : RulesValidator {

    val invalidApiRule = InvalidApiSpecificationRule()

    override fun validate(swaggerContent: String): List<Violation> {
        val swagger = try {
            SwaggerParser().parse(swaggerContent)!!
        } catch (e: Exception) {
            return listOf(invalidApiRule.getGeneralViolation())
        }
        return rules
                .filter { rule -> rulesPolicy.accepts(rule) }
                .map { it.validate(swagger) }
                .filterNotNull()
                .sortedBy { it.violationType }
    }
}

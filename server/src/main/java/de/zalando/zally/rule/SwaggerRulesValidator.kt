package de.zalando.zally.rule

import io.swagger.parser.SwaggerParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * This validator validates a given Swagger definition based
 * on set of rules. It will sort the output by path.
 */
@Component
class SwaggerRulesValidator(@Autowired rules: List<SwaggerRule>,
                            @Autowired rulesPolicy: RulesPolicy,
                            @Autowired invalidApiRule: InvalidApiSchemaRule) : RulesValidator<SwaggerRule>(rules, rulesPolicy, invalidApiRule) {

    @Throws(java.lang.Exception::class)
    override fun createRuleChecker(swaggerContent: String): (SwaggerRule) -> Iterable<Violation> {
        val swagger = SwaggerParser().parse(swaggerContent)!!
        return { rule -> listOfNotNull(rule.validate(swagger)) }
    }

}

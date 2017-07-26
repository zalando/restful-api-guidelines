package de.zalando.zally.rule

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CompositeRulesValidator(
        @Autowired val swaggerRulesValidator: SwaggerRulesValidator,
        @Autowired val jsonRulesValidator: JsonRulesValidator) : ApiValidator {

    override fun validate(swaggerContent: String, ignoreRules: List<String>): List<Violation> =
            swaggerRulesValidator.validate(swaggerContent, ignoreRules) + jsonRulesValidator.validate(swaggerContent, ignoreRules)

}

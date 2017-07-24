package de.zalando.zally.rule

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JsonRulesValidator(@Autowired rules: List<JsonRule>,
                         @Autowired rulesPolicy: RulesPolicy,
                         @Autowired invalidApiRule: InvalidApiSchemaRule) : RulesValidator<JsonRule>(rules, rulesPolicy, invalidApiRule) {

    private val jsonTreeReader = ObjectTreeReader()

    @Throws(java.lang.Exception::class)
    override fun createRuleChecker(swaggerContent: String): (JsonRule) -> Iterable<Violation> {
        val swaggerJson = jsonTreeReader.read(swaggerContent)
        return { rule -> rule.validate(swaggerJson) }
    }

}

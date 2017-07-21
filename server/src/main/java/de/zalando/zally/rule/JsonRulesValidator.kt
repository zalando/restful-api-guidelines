package de.zalando.zally.rule

import de.zalando.json.validation.ObjectTreeReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JsonRulesValidator(@Autowired val rules: List<JsonRule>,
                         @Autowired val rulesPolicy: RulesPolicy) : RulesValidator {

    val invalidApiRule = InvalidApiSpecificationRule()
    val jsonTreeReader = ObjectTreeReader()

    override fun validate(swaggerContent: String): List<Violation> {
        val swaggerJson = try {
            jsonTreeReader.read(swaggerContent)
        } catch (e: Exception) {
            return listOf(invalidApiRule.getGeneralViolation())
        }
        return rules
                .filter { rule -> rulesPolicy.accepts(rule) }
                .flatMap { it.validate(swaggerJson) }
                .filterNotNull()
                .sortedBy { it.violationType }

    }

}

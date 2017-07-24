package de.zalando.zally.rule

abstract class RulesValidator<RuleT>(val rules: List<RuleT>, val rulesPolicy: RulesPolicy, val invalidApiRule: InvalidApiSchemaRule) : ApiValidator where RuleT : Rule {

    override fun validate(swaggerContent: String): List<Violation> {
        val ruleChecker = try {
            createRuleChecker(swaggerContent)
        } catch (e: Exception) {
            return listOf(invalidApiRule.getGeneralViolation())
        }
        return rules
                .filter { rule -> rulesPolicy.accepts(rule) }
                .flatMap(ruleChecker)
                .sortedBy(Violation::violationType)
    }

    @Throws(java.lang.Exception::class)
    abstract fun createRuleChecker(swaggerContent: String): (RuleT) -> Iterable<Violation>
}
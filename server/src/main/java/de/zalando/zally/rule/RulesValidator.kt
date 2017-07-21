package de.zalando.zally.rule

interface RulesValidator {
    fun validate(swaggerContent: String): List<Violation>
}
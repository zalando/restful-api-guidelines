package de.zalando.zally.rule

interface ApiValidator {
    fun validate(swaggerContent: String, ignoreRules: List<String> = emptyList()): List<Violation>
}

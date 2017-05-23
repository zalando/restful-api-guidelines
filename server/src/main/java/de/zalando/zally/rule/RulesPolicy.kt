package de.zalando.zally.rule

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RulesPolicy(@Value("\${zally.ignoreRules:}") val ignoreRules: Array<String>) {
    fun accepts(rule: Rule): Boolean {
        return !ignoreRules.contains(rule.code)
    }
}

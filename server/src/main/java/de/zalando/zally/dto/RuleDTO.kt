package de.zalando.zally.dto

import com.fasterxml.jackson.annotation.JsonProperty
import de.zalando.zally.rule.Rule

data class RuleDTO(

    var title: String? = null,
    var type: ViolationType? = null,
    var url: String? = null,
    var code: String? = null,
    @JsonProperty("is_active") var active: Boolean? = null
) {
    constructor(rule: Rule, isActive: Boolean) :
            this(rule.title, rule.violationType, rule.url, rule.code, isActive)
}

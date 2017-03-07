package de.zalando.zally

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import de.zalando.zally.rules.Rule

data class Violation(
        @JsonIgnore val rule: Rule,
        val title: String,
        val description: String,
        @JsonProperty("violation_type") val violationType: ViolationType,
        @JsonProperty("rule_link")val ruleLink: String,
        val paths: List<String>)
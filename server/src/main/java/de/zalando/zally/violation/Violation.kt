package de.zalando.zally.violation

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import de.zalando.zally.rule.Rule

data class Violation(
    @JsonIgnore val rule: Rule,
    val title: String,
    val description: String,
    @JsonProperty("violation_type") val violationType: ViolationType,
    @JsonProperty("rule_link") val ruleLink: String,
    val paths: List<String>)
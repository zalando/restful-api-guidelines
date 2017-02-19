package de.zalando.zally

import com.fasterxml.jackson.annotation.JsonProperty


data class Violation(
        val title: String,
        val description: String,
        @JsonProperty("violation_type") val violationType: ViolationType,
        @JsonProperty("rule_link")val ruleLink: String,
        val paths: List<String>)
package de.zalando.zally.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import de.zalando.zally.rule.Rule

data class Violation(
    @JsonIgnore val rule: Rule,
    val title: String,
    val description: String,
    val violationType: ViolationType,
    val ruleLink: String,
    val paths: List<String>)

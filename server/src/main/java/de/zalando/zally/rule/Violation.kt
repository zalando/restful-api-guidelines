package de.zalando.zally.rule

import de.zalando.zally.dto.ViolationType

data class Violation(

    val rule: Rule,
    val title: String,
    val description: String,
    val violationType: ViolationType,
    val ruleLink: String,
    val paths: List<String>
)

package de.zalando.zally.rule

import de.zalando.zally.dto.ViolationType

interface Rule {

    val title: String
    val violationType: ViolationType
    val url: String?
    val code: String
    val name: String

}

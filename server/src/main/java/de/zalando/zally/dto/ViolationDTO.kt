package de.zalando.zally.dto

data class ViolationDTO(

    var title: String? = null,
    var description: String? = null,
    var violationType: ViolationType? = null,
    var ruleLink: String? = null,
    var paths: List<String>? = null
)

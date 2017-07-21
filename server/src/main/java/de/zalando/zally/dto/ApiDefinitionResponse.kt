package de.zalando.zally.dto

data class ApiDefinitionResponse (

    var message: String? = null,
    var violations: List<ViolationDTO>? = null,
    var violationsCount: Map<String, Int>? = null
)

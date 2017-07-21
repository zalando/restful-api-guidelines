package de.zalando.zally.dto

data class ApiDefinitionResponse (

    var message: String? = null,
    var violations: List<Violation>? = null,
    var violationsCount: Map<String, Int>? = null
)

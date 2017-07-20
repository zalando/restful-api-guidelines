package de.zalando.zally.violation

/**
 * @author innokenty
 */
data class ApiDefinitionResponse (

    var message: String? = null,
    var violations: List<Violation>? = null,
    var violationsCount: Map<String, Int>? = null
)

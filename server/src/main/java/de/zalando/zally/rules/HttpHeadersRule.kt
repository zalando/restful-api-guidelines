package de.zalando.zally.rules

import de.zalando.zally.Violation
import io.swagger.models.Response
import io.swagger.models.Swagger
import io.swagger.models.parameters.Parameter

abstract class HttpHeadersRule : Rule {
    val PARAMETER_NAMES_WHITELIST = setOf("ETag", "TSV", "TE", "Content-MD5", "DNT", "X-ATT-DeviceId", "X-UIDH",
            "X-Request-ID", "X-Correlation-ID", "WWW-Authenticate", "X-XSS-Protection", "X-Flow-ID", "X-UID",
            "X-Tenant-ID", "X-Device-OS")

    abstract fun createViolation(paths: List<String>): Violation

    abstract fun isViolation(header: String): Boolean

    override fun validate(swagger: Swagger): Violation? {
        fun Collection<Parameter>?.extractHeaders(path: String) =
                orEmpty().filter { it.`in` == "header" }.map { Pair(path, it.name) }

        fun Collection<Response>?.extractHeaders(path: String) =
                orEmpty().flatMap { it.headers?.keys.orEmpty() }.map { Pair(path, it) }

        val fromParams = swagger.parameters.orEmpty().values.extractHeaders("parameters")
        val fromPaths = swagger.paths.orEmpty().entries.flatMap { entry ->
            val (name, path) = entry
            path.parameters.extractHeaders(name) + path.operations.flatMap { operation ->
                operation.parameters.extractHeaders(name) + operation.responses.values.extractHeaders(name)
            }
        }
        val allHeaders = fromParams + fromPaths
        val paths = allHeaders
                .filter { it.second !in PARAMETER_NAMES_WHITELIST && isViolation(it.second) }
                .map { "${it.first} ${it.second}" }
                .toSet()
                .toList()
        return if (paths.isNotEmpty()) createViolation(paths) else null
    }
}

package de.zalando.zally.rules

import de.zalando.zally.Violation
import io.swagger.models.Response
import io.swagger.models.Swagger
import io.swagger.models.parameters.Parameter
import org.springframework.stereotype.Component
import java.util.*

abstract class HttpHeadersRule : Rule {
    val PARAMETER_NAMES_WHITELIST = setOf("ETag", "TSV", "TE", "Content-MD5", "DNT", "X-ATT-DeviceId", "X-UIDH",
            "X-Request-ID", "X-Correlation-ID", "WWW-Authenticate", "X-XSS-Protection", "X-Flow-ID", "X-UID",
            "X-Tenant-ID", "X-Device-OS")

    abstract fun createViolation(header: String, path: Optional<String>): Violation

    abstract fun isViolation(header: String): Boolean

    override fun validate(swagger: Swagger): List<Violation> {
        fun <T> Collection<T>?.orEmpty() = this ?: emptyList()

        fun <T> T?.toOption() = Optional.ofNullable<T>(this)

        fun Collection<Parameter>?.extractHeaders(path: String?) =
                this.orEmpty().filter { it.`in` == "header" }.map { Pair(it.getName(), path.toOption()) }

        fun Collection<Response>?.extractHeaders(path: String?) =
                this.orEmpty().flatMap { it.headers?.keys.orEmpty() }.map { Pair(it, path.toOption()) }

        val fromParams = swagger.parameters.orEmpty().values.extractHeaders(null)
        val fromPaths = swagger.paths.orEmpty().entries.flatMap { entry ->
            val (name, path) = entry
            path.parameters.extractHeaders(name) + path.operations.flatMap { operation ->
                operation.parameters.extractHeaders(name) + operation.responses.values.extractHeaders(name)
            }
        }
        val allHeaders = fromParams + fromPaths
        return allHeaders
                .filter { !PARAMETER_NAMES_WHITELIST.contains(it.first) && isViolation(it.first) }
                .map { createViolation(it.first, it.second) } }
}

package de.zalando.zally.rule

import io.swagger.models.Swagger

abstract class SwaggerRule : AbstractRule() {

    fun accepts(swagger: Swagger): Boolean {
        val ignoredCodes = swagger.vendorExtensions?.get(zallyIgnoreExtension)
        return ignoredCodes == null
                || ignoredCodes !is Iterable<*>
                || code !in ignoredCodes.map { it.toString() }
    }

    abstract fun validate(swagger: Swagger): Violation?

}

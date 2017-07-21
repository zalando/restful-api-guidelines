package de.zalando.zally.rule

import io.swagger.models.Swagger

abstract class SwaggerRule : AbstractRule() {

    abstract fun validate(swagger: Swagger): Violation?

}

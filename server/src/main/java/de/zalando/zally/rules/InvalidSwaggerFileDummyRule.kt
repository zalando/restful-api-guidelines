package de.zalando.zally.rules

import de.zalando.zally.Violation
import io.swagger.models.Swagger

class InvalidSwaggerFileDummyRule : AbstractRule() {

    override fun validate(swagger: Swagger): Violation? {
        throw UnsupportedOperationException("not implemented, this is only a dummy class")
    }
}

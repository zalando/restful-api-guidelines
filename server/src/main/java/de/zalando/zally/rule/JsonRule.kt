package de.zalando.zally.rule

import com.fasterxml.jackson.databind.JsonNode

abstract class JsonRule : AbstractRule() {

    abstract fun validate(swagger: JsonNode): Iterable<Violation>

}

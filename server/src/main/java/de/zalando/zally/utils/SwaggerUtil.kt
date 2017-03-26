package de.zalando.zally.utils

import io.swagger.models.Model
import io.swagger.models.Swagger

fun Swagger.getAllDefinitions(): List<Pair<Model, String>> {
    return definitions.orEmpty().entries.map { it.value  to "#/definitions/${it.key}" }
}



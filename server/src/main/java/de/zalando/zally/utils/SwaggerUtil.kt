package de.zalando.zally.utils

import io.swagger.models.ArrayModel
import io.swagger.models.ComposedModel
import io.swagger.models.Model
import io.swagger.models.ModelImpl
import io.swagger.models.RefModel
import io.swagger.models.Swagger
import io.swagger.models.parameters.BodyParameter
import io.swagger.models.properties.ArrayProperty
import io.swagger.models.properties.MapProperty
import io.swagger.models.properties.ObjectProperty
import io.swagger.models.properties.Property
import io.swagger.models.properties.RefProperty

typealias ObjectDefinition = Map<String, Property>

fun Swagger.getAllJsonObjects(): List<Pair<ObjectDefinition, String>> {
    return paths.orEmpty().entries.flatMap { (path, pathObj) ->
        pathObj.operationMap.orEmpty().flatMap { (verb, operation) ->
            val fromParams = operation.parameters.orEmpty().flatMap { param ->
                if (param is BodyParameter) findJsonObjects(param.schema, "$path $verb ${param.name}") else emptyList()
            }
            val fromResponses = operation.responses.flatMap { (code, response) ->
                findJsonObjects(response.schema, "$path $verb $code")
            }
            (fromParams + fromResponses).toSet().toList()
        }
    }
}

private fun Swagger.findJsonObjects(def: ObjectDefinition?, path: String): List<Pair<ObjectDefinition, String>> {
    return listOf(def.orEmpty() to path) + def.orEmpty().flatMap { (name, property) ->
        findJsonObjects(property, "$path $name")
    }
}

private fun Swagger.findJsonObjects(property: Property?, path: String): List<Pair<ObjectDefinition, String>> =
        when (property) {
            is RefProperty -> {
                val model = definitions.orEmpty()[property.simpleRef]
                findJsonObjects(model, "#/definitions/${property.simpleRef}")
            }
            is ArrayProperty -> findJsonObjects(property.items, "$path items")
            is MapProperty -> findJsonObjects(property.additionalProperties, path)
            is ObjectProperty -> findJsonObjects(property.properties, path)
            else -> emptyList()
        }

private fun Swagger.findJsonObjects(model: Model?, path: String): List<Pair<ObjectDefinition, String>> =
        when (model) {
            is RefModel ->
                findJsonObjects(model.properties, "#/definitions/${model.simpleRef}")
            is ArrayModel ->
                findJsonObjects(model.items, "$path items")
            is ModelImpl ->
                findJsonObjects(model.properties, path)
            is ComposedModel ->
                model.allOf.orEmpty().flatMap { findJsonObjects(it, path) } +
                        model.interfaces.orEmpty().flatMap { findJsonObjects(it, path) } +
                        findJsonObjects(model.parent, path) +
                        findJsonObjects(model.child, path)
            else -> emptyList()
        }

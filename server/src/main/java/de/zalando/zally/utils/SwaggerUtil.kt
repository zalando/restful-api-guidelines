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

data class ObjectDefinition(val definition: Map<String, Property>, val path: String)

fun Swagger.getAllJsonObjects(): List<ObjectDefinition> {
    val visitedPaths = mutableSetOf<String>()
    val fromPaths = paths.orEmpty().entries.flatMap { (path, pathObj) ->
        pathObj.operationMap.orEmpty().flatMap { (verb, operation) ->
            val fromParams = operation.parameters.orEmpty().flatMap { param ->
                if (param is BodyParameter)
                    findJsonObjects(param.schema, "$path $verb ${param.name}", visitedPaths)
                else emptyList()
            }
            val fromResponses = operation.responses.flatMap { (code, response) ->
                findJsonObjects(response.schema, "$path $verb $code", visitedPaths)
            }
            (fromParams + fromResponses)
        }
    }
    val fromDefinitions = definitions.orEmpty().flatMap { (name, def) ->
        findJsonObjects(def, "#/definitions/$name", visitedPaths)
    }
    return (fromPaths + fromDefinitions).toSet().toList()
}

private fun Swagger.findJsonObjects(obj: Map<String, Property>?, path: String, visitedPaths: MutableSet<String>): List<ObjectDefinition> =
    if (path !in visitedPaths) {
        visitedPaths += path
        listOf(ObjectDefinition(obj.orEmpty(), path)) + obj.orEmpty().flatMap { (name, property) ->
            findJsonObjects(property, "$path $name", visitedPaths)
        }
    } else emptyList()

private fun Swagger.findJsonObjects(property: Property?, path: String, visitedPaths: MutableSet<String>): List<ObjectDefinition> =
        when (property) {
            is RefProperty -> {
                val model = definitions.orEmpty()[property.simpleRef]
                findJsonObjects(model, "#/definitions/${property.simpleRef}", visitedPaths)
            }
            is ArrayProperty -> findJsonObjects(property.items, "$path items", visitedPaths)
            is MapProperty -> findJsonObjects(property.additionalProperties, path, visitedPaths)
            is ObjectProperty -> findJsonObjects(property.properties, path, visitedPaths)
            else -> emptyList()
        }

private fun Swagger.findJsonObjects(model: Model?, path: String, visitedPaths: MutableSet<String>): List<ObjectDefinition> =
        when (model) {
            is RefModel ->
                findJsonObjects(model.properties, "#/definitions/${model.simpleRef}", visitedPaths)
            is ArrayModel ->
                findJsonObjects(model.items, "$path items", visitedPaths)
            is ModelImpl ->
                findJsonObjects(model.properties, path, visitedPaths)
            is ComposedModel ->
                model.allOf.orEmpty().flatMap { findJsonObjects(it, path, visitedPaths) } +
                        model.interfaces.orEmpty().flatMap { findJsonObjects(it, path, visitedPaths) } +
                        findJsonObjects(model.parent, path, visitedPaths) +
                        findJsonObjects(model.child, path, visitedPaths)
            else -> emptyList()
        }

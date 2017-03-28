package de.zalando.zally.utils

import io.swagger.models.Model
import io.swagger.models.RefModel
import io.swagger.models.Swagger
import io.swagger.models.parameters.BodyParameter
import io.swagger.models.properties.Property

fun Swagger.getAllDefinitions(): List<Pair<Model, String>> {
    fun findAllModels(property: Property?, path: String): List<Pair<Model, String>> = throw RuntimeException()

    fun findAllModels(model: Model?, path: String): List<Pair<Model, String>> {
        if (model == null) {
            return emptyList()
        }
        //TODO handle arrays
        val curPath = (model as? RefModel)?.simpleRef ?: path
        return listOf(model to curPath) + model.properties.orEmpty().flatMap { (name, prop) ->
            findAllModels(prop, "$curPath $name")
        }
    }


    return paths.orEmpty().entries.flatMap { (path, pathObj) ->
        pathObj.operationMap.orEmpty().flatMap { (verb, operation) ->
            val fromParams = operation.parameters.orEmpty().flatMap { param ->
                if (param is BodyParameter) findAllModels(param.schema, "$path $verb ${param.name}") else emptyList()
            }
            val fromResponses = operation.responses.flatMap { (code, response) ->
                findAllModels(response.schema, "$path $verb $code")
            }
            fromParams + fromResponses
        }
    }
}

//fun findAllRefs(model: Model?): List<String> =
//        when (model) {
//            is RefModel -> listOf(model.simpleRef)
//            is ArrayModel -> findAllRefs(model.items)
//            is ModelImpl ->
//                model.properties.orEmpty().values.flatMap(this::findAllRefs) +
//                        findAllRefs(model.additionalProperties)
//            is ComposedModel ->
//                model.allOf.orEmpty().flatMap(this::findAllRefs) +
//                        model.interfaces.orEmpty().flatMap(this::findAllRefs) +
//                        findAllRefs(model.parent) +
//                        findAllRefs(model.child)
//            else -> emptyList()
//        }
//
//fun findAllRefs(prop: Property?): List<String> =
//        when (prop) {
//            is RefProperty -> listOf(prop.simpleRef)
//            is ArrayProperty -> findAllRefs(prop.items)
//            is MapProperty -> findAllRefs(prop.additionalProperties)
//            is ObjectProperty -> prop.properties.orEmpty().values.flatMap(this::findAllRefs)
//            else -> emptyList()
//        }



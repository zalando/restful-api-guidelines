package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.ArrayModel
import io.swagger.models.ComposedModel
import io.swagger.models.Model
import io.swagger.models.ModelImpl
import io.swagger.models.RefModel
import io.swagger.models.Response
import io.swagger.models.Swagger
import io.swagger.models.parameters.BodyParameter
import io.swagger.models.parameters.Parameter
import io.swagger.models.properties.ArrayProperty
import io.swagger.models.properties.MapProperty
import io.swagger.models.properties.ObjectProperty
import io.swagger.models.properties.Property
import io.swagger.models.properties.RefProperty
import org.springframework.stereotype.Component

@Component
class NoUnusedDefinitionsRule : AbstractRule() {
    override val title = "Do not leave unused definitions"
    override val violationType = ViolationType.SHOULD
    override val url = ""

    override fun validate(swagger: Swagger): Violation? {
        val paramsInPaths = swagger.paths.orEmpty().values.flatMap { path ->
            path.operations.orEmpty().flatMap { operation ->
                operation.parameters.orEmpty()
            }
        }.toSet()

        val refsInPaths = swagger.paths.orEmpty().values.flatMap { path ->
            path.operations.orEmpty().flatMap { operation ->
                val inParams = operation.parameters.orEmpty().flatMap(this::findAllRefs)
                val inResponse = operation.responses.orEmpty().values.flatMap(this::findAllRefs)
                inParams + inResponse
            }
        }
        val refsInDefs = swagger.definitions.orEmpty().values.flatMap(this::findAllRefs)
        val allRefs = (refsInPaths + refsInDefs).toSet()

        val unusedParams = swagger.parameters.orEmpty().filterValues { it !in paramsInPaths }.keys.map { "#/parameters/$it" }
        val unusedDefs = swagger.definitions.orEmpty().keys.filter { it !in allRefs }.map { "#/definitions/$it" }

        val paths = unusedParams + unusedDefs

        return if (paths.isNotEmpty()) {
            Violation(this, title, "Found ${paths.size} unused definitions", violationType, url, paths)
        } else null
    }

    fun findAllRefs(param: Parameter): List<String> =
            if (param is BodyParameter) findAllRefs(param.schema) else emptyList()

    fun findAllRefs(response: Response): List<String> =
            if (response.schema != null) findAllRefs(response.schema) else emptyList()

    fun findAllRefs(model: Model?): List<String> =
            when (model) {
                is RefModel -> listOf(model.simpleRef)
                is ArrayModel -> findAllRefs(model.items)
                is ModelImpl ->
                    model.properties.orEmpty().values.flatMap(this::findAllRefs) +
                            findAllRefs(model.additionalProperties)
                is ComposedModel ->
                    model.allOf.orEmpty().flatMap(this::findAllRefs) +
                            model.interfaces.orEmpty().flatMap(this::findAllRefs) +
                            findAllRefs(model.parent) +
                            findAllRefs(model.child)
                else -> emptyList()
            }

    fun findAllRefs(prop: Property?): List<String> =
            when (prop) {
                is RefProperty -> listOf(prop.simpleRef)
                is ArrayProperty -> findAllRefs(prop.items)
                is MapProperty -> findAllRefs(prop.additionalProperties)
                is ObjectProperty -> prop.properties.orEmpty().values.flatMap(this::findAllRefs)
                else -> emptyList()
            }
}



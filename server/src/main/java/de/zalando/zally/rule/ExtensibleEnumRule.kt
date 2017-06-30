package de.zalando.zally.rule

import de.zalando.zally.violation.Violation
import de.zalando.zally.violation.ViolationType.SHOULD
import io.swagger.models.Operation
import io.swagger.models.Swagger
import io.swagger.models.parameters.Parameter
import io.swagger.models.parameters.SerializableParameter
import io.swagger.models.properties.BinaryProperty
import io.swagger.models.properties.BooleanProperty
import io.swagger.models.properties.DateProperty
import io.swagger.models.properties.DateTimeProperty
import io.swagger.models.properties.DoubleProperty
import io.swagger.models.properties.EmailProperty
import io.swagger.models.properties.FloatProperty
import io.swagger.models.properties.IntegerProperty
import io.swagger.models.properties.LongProperty
import io.swagger.models.properties.PasswordProperty
import io.swagger.models.properties.Property
import org.springframework.stereotype.Component
import io.swagger.models.properties.StringProperty

@Component
class ExtensibleEnumRule : AbstractRule() {
    override val title = "Prefer Compatible Extensions"
    override val url = "http://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
        "#should-prefer-compatible-extensions"
    override val violationType = SHOULD
    override val code = "S012"

    override fun validate(swagger: Swagger): Violation? {
        val properties = enumProperties(swagger)
        val parameters = enumParameters(swagger)

        val enumNames = (properties.keys + parameters.keys).distinct()
        val enumPaths = (properties.values + parameters.values).distinct()
        if (enumNames.isNotEmpty()) return Violation(
            rule = this, violationType = violationType, title = title, ruleLink = url, paths = enumPaths,
            description = "Properties/Parameters $enumNames are not extensible enums")

        return null
    }

    private fun enumProperties(swagger: Swagger): Map<String, String> {
        val propertiesAndPaths = mutableMapOf<String, String>()
        swagger.definitions.orEmpty().forEach { (defName, model) ->
            val enumPropNames = model.properties.orEmpty().filter { (_, prop) -> isEnum(prop) }.map { it.key }
            enumPropNames.forEach { propertyName ->
                propertiesAndPaths.put(propertyName, "#/definitions/$defName/properties/$propertyName")
            }
        }
        return propertiesAndPaths
    }

    private fun enumParameters(swagger: Swagger): Map<String, String> {

        fun enumsIn(operation: Operation?): List<String> {
            fun isEnum(parameter: Parameter?) =
                (parameter as? SerializableParameter)?.enum?.orEmpty()?.isNotEmpty() ?: false

            return operation?.parameters.orEmpty().filter { isEnum(it) }.map { it.name }
        }

        val parametersAndPaths = mutableMapOf<String, String>()

        swagger.paths.orEmpty().forEach { (pathName, path) ->
            path.operationMap.orEmpty().forEach { (opName, op) ->
                enumsIn(op).forEach { parameterName ->
                    parametersAndPaths.put(parameterName, "#/paths$pathName/$opName/parameters/$parameterName")
                }
            }
        }

        return parametersAndPaths
    }

    private fun isEnum(property: Property): Boolean {
        fun <T> isNotEmpty(list: List<T>?) = list.orEmpty().isNotEmpty()

        return when (property) {
            is StringProperty -> isNotEmpty(property.enum)
            is BinaryProperty -> isNotEmpty(property.enum)
            is DateProperty -> isNotEmpty(property.enum)
            is DateTimeProperty -> isNotEmpty(property.enum)
            is BooleanProperty -> isNotEmpty(property.enum)
            is DoubleProperty -> isNotEmpty(property.enum)
            is EmailProperty -> isNotEmpty(property.enum)
            is FloatProperty -> isNotEmpty(property.enum)
            is IntegerProperty -> isNotEmpty(property.enum)
            is LongProperty -> isNotEmpty(property.enum)
            is PasswordProperty -> isNotEmpty(property.enum)
            else -> false
        }
    }
}

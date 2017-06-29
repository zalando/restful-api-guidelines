package de.zalando.zally.rule

import de.zalando.zally.violation.Violation
import de.zalando.zally.violation.ViolationType.SHOULD
import io.swagger.models.Model
import io.swagger.models.Operation
import io.swagger.models.Path
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
        val enumProperties = enumPropertiesInDefinitions(swagger)
        val enumParameters = enumParametersInPaths(swagger)

        val enumNames = (enumProperties + enumParameters).distinct()
        if (enumNames.isNotEmpty()) {
            val pathsWithEnumParameters = pathsWithEnumParameters(swagger, enumParameters)
            val pathsWithEnumProperties = pathWithEnumDefinitions(swagger)

            return Violation(rule = this, violationType = violationType, title = title, ruleLink = url,
                paths = (pathsWithEnumProperties + pathsWithEnumParameters).distinct(),
                description = "Properties/Parameters $enumNames are not extensible enums")
        } else
            return null
    }

    private fun enumPropertiesInDefinitions(swagger: Swagger): List<String> = swagger.definitions.orEmpty().entries
        .flatMap { (_, def) -> def.properties.orEmpty().filter { (_, prop) -> isEnum(prop) }.map { it.key } }

    private fun enumParametersInPaths(swagger: Swagger): List<String> {
        fun isEnum(parameter: Parameter?) =
            (parameter as? SerializableParameter)?.enum?.orEmpty()?.isNotEmpty() ?: false

        return swagger.paths.orEmpty().values
            .flatMap { it.get?.parameters.orEmpty().filter { isEnum(it) } }
            .map { it.name }
    }

    private fun pathsWithEnumParameters(swagger: Swagger, parameters: List<String>) = swagger.paths.orEmpty()
        .filter { hasParameters(it.value, parameters) }.map { it.key }

    private fun pathWithEnumDefinitions(swagger: Swagger): List<String> {
        fun enumProperties(def: Model?): List<String> = def?.properties.orEmpty()
            .filter { (_, prop) -> isEnum(prop) }
            .map { it.key }

        fun definitionsWithEnums(): List<String> = swagger.definitions.orEmpty().entries
            .filter { (_, def) -> enumProperties(def).isNotEmpty() }
            .map { it.key }

        return swagger.paths.orEmpty().entries
            .filter { (_, path) -> hasParameters(path, definitionsWithEnums()) }
            .map { it.key }
    }

    private fun hasParameters(path: Path?, names: List<String>): Boolean {
        fun isIn(op: Operation?): Boolean = op?.parameters.orEmpty().any { it.name in names }

        return isIn(path?.post) || isIn(path?.get) || isIn(path?.put) || isIn(path?.delete) || isIn(path?.options)
            || isIn(path?.head) || isIn(path?.patch)
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

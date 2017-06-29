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
        val definitionsWithEnumProperties = definitionsWithEnums(swagger)

        val pathsWithDefinitionsWithEnums = swagger.paths.orEmpty().entries
            .filter { (_, path) -> havingParameters(path, definitionsWithEnumProperties) }
            .map { it.key }

        val enumParameters = swagger.paths.orEmpty().values
            .flatMap{ it.get?.parameters.orEmpty().filter { isEnum(it) } }
            .map { it.name }

        val pathsWithEnumParameters = swagger.paths.orEmpty().filter { havingParameters(it.value, enumParameters) }.keys


        val enums = (enumProperties + enumParameters).distinct()
        val paths = (pathsWithDefinitionsWithEnums + pathsWithEnumParameters).distinct()

        if (enums.isNotEmpty())
            return Violation(rule = this, paths = paths, ruleLink = url, violationType = violationType,
                title = title, description = "Properties/Fields $enums are not extensible enums")
        else
            return null
    }

    private fun havingParameters(path: Path?, names: List<String>): Boolean {
        fun isIn(op: Operation?): Boolean = op?.parameters.orEmpty().any { it.name in names }

        return isIn(path?.post) || isIn(path?.get) || isIn(path?.put) || isIn(path?.delete) || isIn(path?.options)
            || isIn(path?.head) || isIn(path?.patch)
    }

    private fun isEnum(parameter: Parameter?) = (parameter as? SerializableParameter)?.enum?.orEmpty()?.isNotEmpty() ?: false

    private fun definitionsWithEnums(swagger: Swagger): List<String> = swagger.definitions.orEmpty().entries
        .filter { (_, def) -> enumProperties(def).isNotEmpty() }
        .map { it.key }

    private fun enumPropertiesInDefinitions(swagger: Swagger): List<String> = swagger.definitions.orEmpty().entries
        .flatMap { (_, def) -> def.properties.orEmpty().filter { (_, prop) -> isEnum(prop) }.map { it.key } }

    private fun enumProperties(def: Model?): List<String> {
        return def?.properties.orEmpty()
            .filter { (_, prop) -> isEnum(prop) }
            .map { it.key }
    }

    private fun isEnum(property: Property): Boolean {
        fun <T> notEmpty(values: List<T>?) = values.orEmpty().isNotEmpty()

        return when (property) {
            is StringProperty -> notEmpty(property.enum)
            is BinaryProperty -> notEmpty(property.enum)
            is DateProperty -> notEmpty(property.enum)
            is DateTimeProperty -> notEmpty(property.enum)
            is BooleanProperty -> notEmpty(property.enum)
            is DoubleProperty -> notEmpty(property.enum)
            is EmailProperty -> notEmpty(property.enum)
            is FloatProperty -> notEmpty(property.enum)
            is IntegerProperty -> notEmpty(property.enum)
            is LongProperty -> notEmpty(property.enum)
            is PasswordProperty -> notEmpty(property.enum)
            else -> false
        }
    }

    fun Property.hasEnumValues(): Boolean {
        fun <T> notEmpty(values: List<T>) = values.orEmpty().isNotEmpty()

        return when (this) {
            is StringProperty -> notEmpty(enum)
            is BinaryProperty -> notEmpty(enum)
            is DateProperty -> notEmpty(enum)
            is DateTimeProperty -> notEmpty(enum)
            is BooleanProperty -> notEmpty(enum)
            is DoubleProperty -> notEmpty(enum)
            is EmailProperty -> notEmpty(enum)
            is FloatProperty -> notEmpty(enum)
            is IntegerProperty -> notEmpty(enum)
            is LongProperty -> notEmpty(enum)
            is PasswordProperty -> notEmpty(enum)
            else -> false
        }
    }

}

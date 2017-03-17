package de.zalando.zally.rules

import com.typesafe.config.Config
import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AvoidSynonymsRule(@Autowired rulesConfig: Config) : AbstractRule() {

    val TITLE = "Use common property names"
    val DESC_PATTERN = "Property names should utilize common dictionary"
    val RULE_URL = ""

    @Suppress("UNCHECKED_CAST")
    private val COMMON_DICTIONARY = rulesConfig.getConfig(javaClass.simpleName + ".dictionary")
            .entrySet()
            .map { (key, config) -> key to config.unwrapped() as List<String> }

    override fun validate(swagger: Swagger): Violation? {
        val dict = COMMON_DICTIONARY.flatMap { (right, wrongList) -> wrongList.map { it to right } }.toMap()
        val res = swagger.definitions.orEmpty().flatMap { (defName, def) ->
            val props = def?.properties.orEmpty()
            props.keys.filter { it in dict }.map { it to "#/definitions/$defName" }
        }
        return if (res.isNotEmpty()) {
            val (names, paths) = res.unzip()
            val details = names.toSet().groupBy(dict::get)
                    .map { (right, wrongList) -> right + " instead of " + wrongList.joinToString(", ") }
                    .joinToString("\n")
            Violation(this, TITLE, "$DESC_PATTERN:\n$details", ViolationType.SHOULD, RULE_URL, paths.toSet().toList())
        } else null
    }
}
package de.zalando.zally.rule

import com.typesafe.config.Config
import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import de.zalando.zally.util.getAllJsonObjects
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AvoidJavascriptKeywordsRule(@Autowired rulesConfig: Config) : AbstractRule() {

    override val title = "Avoid reserved Javascript keywords"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
        "#should-reserved-javascript-keywords-should-be-avoided"
    override val violationType = ViolationType.SHOULD
    override val code = "S001"
    private val DESC_PATTERN = "Property names should not coincide with reserved javascript keywords"

    private val blacklist = rulesConfig.getConfig(name).getStringList("blacklist")

    override fun validate(swagger: Swagger): Violation? {
        val result = swagger.getAllJsonObjects().flatMap { (def, path) ->
            val badProps = def.keys.filter { it in blacklist }
            if (badProps.isNotEmpty()) listOf(path + ": " + badProps.joinToString(", ") to path) else emptyList()
        }
        return if (result.isNotEmpty()) {
            val (props, paths) = result.unzip()
            Violation(this, title, DESC_PATTERN + "\n" + props.joinToString("\n"), violationType, url, paths)
        } else null
    }
}

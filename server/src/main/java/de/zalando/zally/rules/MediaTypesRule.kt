package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil.isApplicationJsonOrProblemJson
import de.zalando.zally.utils.PatternUtil.isCustomMediaTypeWithVersioning
import io.swagger.models.Swagger
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class MediaTypesRule : AbstractRule() {
    val TITLE = "Prefer standard media type names"
    val DESCRIPTION = "Custom media types should only be used for versioning"
    val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/data-formats/DataFormats.html" +
            "#should-prefer-standard-media-type-name-applicationjson"

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().entries.flatMap { (pathName, path) ->
            path.operationMap.orEmpty().entries.flatMap { (verb, operation) ->
                val mediaTypes = ArrayList<String>() + operation.produces.orEmpty() + operation.consumes.orEmpty()
                val violatingMediaTypes = mediaTypes.filter(this::isViolatingMediaType)
                if (violatingMediaTypes.isNotEmpty()) listOf("$pathName $verb") else emptyList()
            }
        }
        return if (paths.isNotEmpty()) Violation(this, TITLE, DESCRIPTION, ViolationType.SHOULD, RULE_LINK, paths) else null
    }

    private fun isViolatingMediaType(mediaType: String) =
            !isApplicationJsonOrProblemJson(mediaType) && !isCustomMediaTypeWithVersioning(mediaType)
}

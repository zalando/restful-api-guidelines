package de.zalando.zally.rule

import com.typesafe.config.Config
import de.zalando.zally.dto.ViolationType
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LimitNumberOfResourcesRule(@Autowired rulesConfig: Config) : AbstractRule() {
    override val title = "Limit number of Resources"
    override val url = "http://zalando.github.io/restful-api-guidelines/resources/Resources.html" +
        "#should-limit-number-of-resources"
    override val violationType = ViolationType.SHOULD
    override val code = "S002"
    private val pathCountLimit = rulesConfig.getConfig(name).getInt("paths_count_limit")

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty()
        val pathsCount = paths.size
        return if (pathsCount > pathCountLimit) {
            Violation(this, title, "Number of paths $pathsCount is greater than $pathCountLimit",
                violationType, url, paths.keys.toList())
        } else null
    }
}

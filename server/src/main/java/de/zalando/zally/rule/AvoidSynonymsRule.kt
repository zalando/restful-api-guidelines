package de.zalando.zally.rule

import com.typesafe.config.Config
import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import de.zalando.zally.util.getAllJsonObjects
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices
import org.springframework.stereotype.Component

@Component
class AvoidSynonymsRule(
    @Autowired rulesConfig: Config, @Autowired metricServices: DropwizardMetricServices) : AbstractRule() {

    override val title = "Use common property names"
    // TODO: Provide URL
    override val url = ""
    override val violationType = ViolationType.SHOULD
    override val code = "S010"

    private val descPattern = "Property names should utilize common dictionary"
    private val metricServices = metricServices

    @Suppress("UNCHECKED_CAST")
    private val commonDictionary = rulesConfig.getConfig("$name.dictionary").entrySet()
        .map { (key, config) -> key to config.unwrapped() as List<String> }

    override fun validate(swagger: Swagger): Violation? {
        return validate(swagger, false)
    }

    fun validate(swagger: Swagger, returnViolation: Boolean): Violation? {
        val reversedDictionary = commonDictionary.flatMap { (canonical, synonyms) -> synonyms.map { it to canonical } }.toMap()
        val result = swagger.getAllJsonObjects().flatMap { (def, path) ->
            def.keys.filter { it in reversedDictionary }.map { it to path }
        }

        if (result.isEmpty()) {
            return null
        }

        val (names, paths) = result.unzip()
        submitStatistics(names)

        // TODO: Start returning violations once we'll define a proper dictionary (#301)
        return if (returnViolation) {
            val details = names.toSet().groupBy(reversedDictionary::get)
                .map { (canonical, synonyms) -> canonical + " instead of " + synonyms.joinToString(", ") }
                .joinToString("\n")
            Violation(this, title, "$descPattern:\n$details", violationType, url, paths.toSet().toList())
        } else {
            null
        }
    }

    fun submitStatistics(names: List<String>) {
        names.groupBy { it }.map {
            metricServices.submit(
                "histogram.rules.avoid-synonyms.synonym." + it.key, it.value.count().toDouble()
            )
        }
    }
}

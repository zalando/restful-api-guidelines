package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import io.swagger.parser.SwaggerParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.util.Collections
import java.util.LinkedList
import java.util.stream.Collectors

/**
 * This validator validates a given Swagger definition based
 * on set of rules. It will sort the output by path.
 */
@Component
class RulesValidator
@Autowired
constructor(rules: List<Rule>) {

    private val rules: List<Rule>

    init {
        this.rules = LinkedList(rules)
    }

    fun validate(swaggerContent: String): List<Violation> {
        val swagger: Swagger = try {
            SwaggerParser().parse(swaggerContent)
        } catch (e: Exception) {
            return listOf(Violation("Can't parse swagger file", e, ViolationType.MUST, ""))
        }

        return if (swagger != null)
            validate(swagger)
        else
    }

    fun validate(swagger: Swagger): List<Violation> {
        return rules.flatMap { rule ->
                try {
                    rule.validate(swagger)
                } catch (e: Exception) {
                    LOGGER.warn("Rule thrown an exception during validation", e)
                    emptyList()
                }
            }
            .sortedBy { it.path }
    }

    /**
     * For testing purpose
     */
    fun getRules(): List<Rule> {
        return LinkedList(rules)
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(RulesValidator::class.java)
    }
}
